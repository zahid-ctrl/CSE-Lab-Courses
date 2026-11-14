// ============================================================================
//  SMART PARKING CHALLENGE
//  2D parking simulation game - C++ / OpenGL 3.3 (GLFW + GLAD + GLM)
//
//  Built on top of the Day 7 class code:
//    * same GLFW window + GLAD setup
//    * same inline vertex / fragment shader strings
//    * "uniform vec4 ourColor"  -> every shape gets its colour from the CPU
//    * "uniform mat4 transform" -> every shape is moved / rotated / scaled with GLM
//    * same processInput() + framebuffer_size_callback()
//
//  The whole game is drawn with ONE rectangle (two triangles). Every object
//  (car, wall, cone, parking lines, HUD bars, even the letters) is that same
//  rectangle with a different transform and colour.
// ============================================================================

#define NOMINMAX            // stop windows.h (included by glad.h) from defining min/max macros

#include "glad.h"
#include "glfw3.h"

#include "glm/glm/glm.hpp"
#include "glm/glm/gtc/matrix_transform.hpp"
#include "glm/glm/gtc/type_ptr.hpp"

#include <iostream>
#include <vector>
#include <string>
#include <map>
#include <cmath>
#include <cstdio>
#include <cctype>
#include <algorithm>

void framebuffer_size_callback(GLFWwindow* window, int width, int height); // adjusts the viewport on window resize.
void processInput(GLFWwindow *window); //handles keyboard input (e.g., ESC to close the window).

// settings
const unsigned int SCR_WIDTH = 800;
const unsigned int SCR_HEIGHT = 600;
const float PI = 3.14159265f;

// ----------------------------------------------------------------------------
// Shaders (same idea as class: colour = uniform, plus a transform matrix)
// ----------------------------------------------------------------------------
const char *vertexShaderSource = "#version 330 core\n"
    "layout (location = 0) in vec3 aPos;\n"
    "uniform mat4 transform;\n"
    "void main()\n"
    "{\n"
    "   gl_Position = transform * vec4(aPos, 1.0);\n"
    "}\0";

const char *fragmentShaderSource = "#version 330 core\n"
    "out vec4 FragColor;\n"
    "uniform vec4 ourColor;\n"      // colour comes from the CPU side (uniform), alpha is used for transparency
    "void main()\n"
    "{\n"
    "   FragColor = ourColor;\n"
    "}\n\0";

// ----------------------------------------------------------------------------
// Drawing globals
// ----------------------------------------------------------------------------
unsigned int gShaderProgram = 0;
unsigned int gVAO = 0;
int gTransformLoc = -1;
int gColorLoc = -1;
glm::mat4 gProjection;   // maps game coordinates (0..800, 0..600 pixels) to OpenGL clip space

// One unit rectangle (centered at 0,0, size 1x1) = 2 triangles.
// We scale it to any width/height and rotate/translate it with GLM.
void drawRect(float cx, float cy, float w, float h, float angle,
              float r, float g, float b, float a = 1.0f)
{
    glm::mat4 model = glm::mat4(1.0f);                                   // start from identity matrix
    model = glm::translate(model, glm::vec3(cx, cy, 0.0f));              // 3) move to position
    model = glm::rotate(model, angle, glm::vec3(0.0f, 0.0f, 1.0f));      // 2) rotate around Z axis
    model = glm::scale(model, glm::vec3(w, h, 1.0f));                    // 1) scale unit square to w x h
    glm::mat4 transform = gProjection * model;

    glUniformMatrix4fv(gTransformLoc, 1, GL_FALSE, glm::value_ptr(transform));
    glUniform4f(gColorLoc, r, g, b, a);
    glDrawArrays(GL_TRIANGLES, 0, 6);
}

// Same as drawRect but the offset (ox, oy) is measured in the rotated frame of a parent object.
// Used to build the car out of parts (windshield, wheels, lights...).
void drawRectRel(float cx, float cy, float ang, float ox, float oy, float w, float h,
                 float r, float g, float b, float a = 1.0f)
{
    float ca = cosf(ang), sa = sinf(ang);
    drawRect(cx + ox * ca - oy * sa, cy + ox * sa + oy * ca, w, h, ang, r, g, b, a);
}

// ----------------------------------------------------------------------------
// Tiny 5x7 pixel font - each letter is drawn as small rectangles
// (so we don't need any texture / font library)
// ----------------------------------------------------------------------------
struct Glyph { char c; unsigned char rows[7]; };

const Glyph GLYPHS[] = {
    {'A', {0b01110,0b10001,0b10001,0b11111,0b10001,0b10001,0b10001}},
    {'B', {0b11110,0b10001,0b10001,0b11110,0b10001,0b10001,0b11110}},
    {'C', {0b01110,0b10001,0b10000,0b10000,0b10000,0b10001,0b01110}},
    {'D', {0b11110,0b10001,0b10001,0b10001,0b10001,0b10001,0b11110}},
    {'E', {0b11111,0b10000,0b10000,0b11110,0b10000,0b10000,0b11111}},
    {'F', {0b11111,0b10000,0b10000,0b11110,0b10000,0b10000,0b10000}},
    {'G', {0b01110,0b10001,0b10000,0b10111,0b10001,0b10001,0b01111}},
    {'H', {0b10001,0b10001,0b10001,0b11111,0b10001,0b10001,0b10001}},
    {'I', {0b01110,0b00100,0b00100,0b00100,0b00100,0b00100,0b01110}},
    {'J', {0b00111,0b00010,0b00010,0b00010,0b00010,0b10010,0b01100}},
    {'K', {0b10001,0b10010,0b10100,0b11000,0b10100,0b10010,0b10001}},
    {'L', {0b10000,0b10000,0b10000,0b10000,0b10000,0b10000,0b11111}},
    {'M', {0b10001,0b11011,0b10101,0b10101,0b10001,0b10001,0b10001}},
    {'N', {0b10001,0b11001,0b10101,0b10011,0b10001,0b10001,0b10001}},
    {'O', {0b01110,0b10001,0b10001,0b10001,0b10001,0b10001,0b01110}},
    {'P', {0b11110,0b10001,0b10001,0b11110,0b10000,0b10000,0b10000}},
    {'Q', {0b01110,0b10001,0b10001,0b10001,0b10101,0b10010,0b01101}},
    {'R', {0b11110,0b10001,0b10001,0b11110,0b10100,0b10010,0b10001}},
    {'S', {0b01111,0b10000,0b10000,0b01110,0b00001,0b00001,0b11110}},
    {'T', {0b11111,0b00100,0b00100,0b00100,0b00100,0b00100,0b00100}},
    {'U', {0b10001,0b10001,0b10001,0b10001,0b10001,0b10001,0b01110}},
    {'V', {0b10001,0b10001,0b10001,0b10001,0b10001,0b01010,0b00100}},
    {'W', {0b10001,0b10001,0b10001,0b10101,0b10101,0b11011,0b10001}},
    {'X', {0b10001,0b10001,0b01010,0b00100,0b01010,0b10001,0b10001}},
    {'Y', {0b10001,0b10001,0b01010,0b00100,0b00100,0b00100,0b00100}},
    {'Z', {0b11111,0b00001,0b00010,0b00100,0b01000,0b10000,0b11111}},
    {'0', {0b01110,0b10001,0b10011,0b10101,0b11001,0b10001,0b01110}},
    {'1', {0b00100,0b01100,0b00100,0b00100,0b00100,0b00100,0b01110}},
    {'2', {0b01110,0b10001,0b00001,0b00010,0b00100,0b01000,0b11111}},
    {'3', {0b11110,0b00001,0b00001,0b01110,0b00001,0b00001,0b11110}},
    {'4', {0b00010,0b00110,0b01010,0b10010,0b11111,0b00010,0b00010}},
    {'5', {0b11111,0b10000,0b11110,0b00001,0b00001,0b10001,0b01110}},
    {'6', {0b00110,0b01000,0b10000,0b11110,0b10001,0b10001,0b01110}},
    {'7', {0b11111,0b00001,0b00010,0b00100,0b01000,0b01000,0b01000}},
    {'8', {0b01110,0b10001,0b10001,0b01110,0b10001,0b10001,0b01110}},
    {'9', {0b01110,0b10001,0b10001,0b01111,0b00001,0b00010,0b01100}},
    {':', {0b00000,0b00100,0b00000,0b00000,0b00000,0b00100,0b00000}},
    {'-', {0b00000,0b00000,0b00000,0b11111,0b00000,0b00000,0b00000}},
    {'/', {0b00001,0b00001,0b00010,0b00100,0b01000,0b10000,0b10000}},
    {'!', {0b00100,0b00100,0b00100,0b00100,0b00100,0b00000,0b00100}},
    {'.', {0b00000,0b00000,0b00000,0b00000,0b00000,0b01100,0b01100}},
    {'+', {0b00000,0b00100,0b00100,0b11111,0b00100,0b00100,0b00000}},
    {',', {0b00000,0b00000,0b00000,0b00000,0b01100,0b00100,0b01000}},
};
const int GLYPH_COUNT = sizeof(GLYPHS) / sizeof(GLYPHS[0]);

float textWidth(const std::string& s, float ps) { return s.empty() ? 0.0f : s.size() * 6.0f * ps - ps; }

// (x, y) = bottom-left corner of the text, ps = size of one "pixel"
void drawText(const std::string& s, float x, float y, float ps, float r, float g, float b, float a = 1.0f)
{
    for (size_t i = 0; i < s.size(); i++)
    {
        char c = (char)toupper((unsigned char)s[i]);
        for (int k = 0; k < GLYPH_COUNT; k++)
        {
            if (GLYPHS[k].c != c) continue;
            for (int row = 0; row < 7; row++)
                for (int col = 0; col < 5; col++)
                    if (GLYPHS[k].rows[row] & (1 << (4 - col)))
                        drawRect(x + i * 6.0f * ps + col * ps + ps * 0.5f,
                                 y + (6 - row) * ps + ps * 0.5f, ps, ps, 0.0f, r, g, b, a);
            break;
        }
    }
}
void drawTextCentered(const std::string& s, float cx, float y, float ps, float r, float g, float b, float a = 1.0f)
{
    drawText(s, cx - textWidth(s, ps) * 0.5f, y, ps, r, g, b, a);
}
void drawTextRight(const std::string& s, float rx, float y, float ps, float r, float g, float b, float a = 1.0f)
{
    drawText(s, rx - textWidth(s, ps), y, ps, r, g, b, a);
}

// ----------------------------------------------------------------------------
// Geometry + collision detection (rotated rectangles, Separating Axis Theorem)
// ----------------------------------------------------------------------------
struct Rect { float x, y, w, h, angle; };   // centre, size, rotation (radians)

void getCorners(const Rect& r, glm::vec2 c[4])
{
    float ca = cosf(r.angle), sa = sinf(r.angle);
    glm::vec2 ax(ca, sa), ay(-sa, ca);                    // the rectangle's own X and Y directions
    glm::vec2 ctr(r.x, r.y);
    glm::vec2 hx = ax * (r.w * 0.5f), hy = ay * (r.h * 0.5f);
    c[0] = ctr - hx - hy;
    c[1] = ctr + hx - hy;
    c[2] = ctr + hx + hy;
    c[3] = ctr - hx + hy;
}

// Two rotated rectangles overlap only if NO separating line exists between them.
// It is enough to test the 4 edge directions of the two rectangles.
bool rectsOverlap(const Rect& a, const Rect& b)
{
    glm::vec2 ca[4], cb[4];
    getCorners(a, ca);
    getCorners(b, cb);
    glm::vec2 axes[4] = { ca[1] - ca[0], ca[3] - ca[0], cb[1] - cb[0], cb[3] - cb[0] };

    for (int i = 0; i < 4; i++)
    {
        glm::vec2 axis = glm::normalize(axes[i]);
        float minA = 1e9f, maxA = -1e9f, minB = 1e9f, maxB = -1e9f;
        for (int j = 0; j < 4; j++)
        {
            float pa = glm::dot(ca[j], axis);
            float pb = glm::dot(cb[j], axis);
            minA = std::min(minA, pa);  maxA = std::max(maxA, pa);
            minB = std::min(minB, pb);  maxB = std::max(maxB, pb);
        }
        if (maxA < minB || maxB < minA) return false;     // gap found -> no collision
    }
    return true;
}

// ----------------------------------------------------------------------------
// Game objects
// ----------------------------------------------------------------------------
const float CAR_L = 56.0f;          // car length
const float CAR_W = 28.0f;          // car width
const float WHEELBASE = 36.0f;      // used for the steering physics
const float MAX_STEER = 0.6f;       // max steering angle (radians)

// playable area (the dark strip on top is the HUD)
const float PLAY_MIN_X = 10.0f, PLAY_MAX_X = 790.0f;
const float PLAY_MIN_Y = 10.0f, PLAY_MAX_Y = 540.0f;

struct Car
{
    glm::vec2 pos;
    float angle;     // heading (0 = facing right)
    float speed;     // pixels / second (negative = reversing)
    float steer;     // current steering wheel angle
};

Rect carRect(const Car& c) { return Rect{ c.pos.x, c.pos.y, CAR_L, CAR_W, c.angle }; }

enum ObstacleKind { OBS_CAR, OBS_CONE, OBS_BLOCK };

struct Obstacle
{
    Rect r;
    ObstacleKind kind;
    glm::vec3 color;
    bool moving;            // patrolling obstacle (moves between 'from' and 'to')
    glm::vec2 from, to;
    float speed;
    float t;                // 0..1 position on the patrol path
    int dir;                // +1 / -1
    float baseAngle;
};

struct Level
{
    const char* name;
    Rect spot;              // the target parking space (long side = local x axis)
    float carX, carY, carAngle;
    float timeLimit;        // seconds
    float angleTolDeg;      // how well the car must be aligned with the space
    std::vector<Obstacle> obstacles;
};

Obstacle makeParkedCar(float x, float y, float ang, glm::vec3 col)
{
    Obstacle o;
    o.r = Rect{ x, y, CAR_L, CAR_W, ang };
    o.kind = OBS_CAR; o.color = col; o.moving = false;
    o.from = o.to = glm::vec2(x, y); o.speed = 0.0f; o.t = 0.0f; o.dir = 1; o.baseAngle = ang;
    return o;
}
Obstacle makeCone(float x, float y)
{
    Obstacle o;
    o.r = Rect{ x, y, 18.0f, 18.0f, 0.0f };
    o.kind = OBS_CONE; o.color = glm::vec3(1.0f, 0.5f, 0.1f); o.moving = false;
    o.from = o.to = glm::vec2(x, y); o.speed = 0.0f; o.t = 0.0f; o.dir = 1; o.baseAngle = 0.0f;
    return o;
}
Obstacle makeBlock(float x, float y, float w, float h)
{
    Obstacle o;
    o.r = Rect{ x, y, w, h, 0.0f };
    o.kind = OBS_BLOCK; o.color = glm::vec3(0.45f, 0.45f, 0.52f); o.moving = false;
    o.from = o.to = glm::vec2(x, y); o.speed = 0.0f; o.t = 0.0f; o.dir = 1; o.baseAngle = 0.0f;
    return o;
}
Obstacle makePatrol(float x1, float y1, float x2, float y2, float speed, glm::vec3 col)
{
    Obstacle o;
    float ang = atan2f(y2 - y1, x2 - x1);
    o.r = Rect{ x1, y1, CAR_L, CAR_W, ang };
    o.kind = OBS_CAR; o.color = col; o.moving = true;
    o.from = glm::vec2(x1, y1); o.to = glm::vec2(x2, y2);
    o.speed = speed; o.t = 0.0f; o.dir = 1; o.baseAngle = ang;
    return o;
}

std::vector<Level> gLevels;

void buildLevels()
{
    glm::vec3 blue(0.25f, 0.45f, 0.85f), green(0.25f, 0.7f, 0.4f), yellow(0.95f, 0.8f, 0.2f);
    glm::vec3 purple(0.6f, 0.35f, 0.8f), white(0.85f, 0.85f, 0.88f), taxi(0.98f, 0.75f, 0.1f);

    // ---- Level 1 : big space, almost no obstacles -------------------------
    {
        Level L;
        L.name = "WARM UP";
        L.spot = Rect{ 640, 270, 96, 52, 0 };
        L.carX = 110; L.carY = 270; L.carAngle = 0;
        L.timeLimit = 60; L.angleTolDeg = 20;
        L.obstacles.push_back(makeCone(300, 200));
        L.obstacles.push_back(makeCone(300, 340));
        L.obstacles.push_back(makeCone(450, 270));
        gLevels.push_back(L);
    }
    // ---- Level 2 : perpendicular space between two parked cars ------------
    {
        Level L;
        L.name = "BETWEEN CARS";
        L.spot = Rect{ 400, 110, 84, 46, PI / 2 };
        L.carX = 110; L.carY = 400; L.carAngle = 0;
        L.timeLimit = 60; L.angleTolDeg = 18;
        L.obstacles.push_back(makeParkedCar(350, 110, PI / 2, blue));
        L.obstacles.push_back(makeParkedCar(450, 110, PI / 2, green));
        L.obstacles.push_back(makeParkedCar(240, 110, PI / 2, yellow));
        L.obstacles.push_back(makeParkedCar(560, 110, PI / 2, white));
        L.obstacles.push_back(makeCone(250, 300));
        L.obstacles.push_back(makeCone(560, 260));
        L.obstacles.push_back(makeBlock(400, 280, 36, 36));
        gLevels.push_back(L);
    }
    // ---- Level 3 : tight space + pillars to weave through ------------------
    {
        Level L;
        L.name = "TIGHT SQUEEZE";
        L.spot = Rect{ 640, 100, 80, 40, PI / 2 };
        L.carX = 80; L.carY = 470; L.carAngle = 0;
        L.timeLimit = 55; L.angleTolDeg = 15;
        L.obstacles.push_back(makeParkedCar(596, 100, PI / 2, purple));
        L.obstacles.push_back(makeParkedCar(684, 100, PI / 2, blue));
        L.obstacles.push_back(makeBlock(250, 380, 34, 34));
        L.obstacles.push_back(makeBlock(250, 230, 34, 34));
        L.obstacles.push_back(makeBlock(430, 320, 34, 34));
        L.obstacles.push_back(makeBlock(430, 170, 34, 34));
        L.obstacles.push_back(makeBlock(550, 430, 34, 34));
        L.obstacles.push_back(makeCone(120, 200));
        gLevels.push_back(L);
    }
    // ---- Level 4 : parallel parking against the bottom wall -----------------
    {
        Level L;
        L.name = "PARALLEL PARK";
        L.spot = Rect{ 450, 48, 92, 38, 0 };
        L.carX = 100; L.carY = 300; L.carAngle = 0;
        L.timeLimit = 55; L.angleTolDeg = 14;
        L.obstacles.push_back(makeParkedCar(364, 48, 0, green));
        L.obstacles.push_back(makeParkedCar(536, 48, 0, yellow));
        L.obstacles.push_back(makeParkedCar(200, 48, 0, purple));
        L.obstacles.push_back(makeParkedCar(700, 48, 0, white));
        L.obstacles.push_back(makeBlock(300, 170, 34, 34));
        L.obstacles.push_back(makeBlock(620, 180, 34, 34));
        L.obstacles.push_back(makeBlock(450, 330, 40, 40));
        L.obstacles.push_back(makeCone(200, 230));
        gLevels.push_back(L);
    }
    // ---- Level 5 : rush hour - two moving cars patrol the lot ---------------
    {
        Level L;
        L.name = "RUSH HOUR";
        L.spot = Rect{ 680, 100, 76, 38, PI / 2 };
        L.carX = 70; L.carY = 480; L.carAngle = 0;
        L.timeLimit = 60; L.angleTolDeg = 12;
        L.obstacles.push_back(makeParkedCar(638, 100, PI / 2, blue));
        L.obstacles.push_back(makeParkedCar(722, 100, PI / 2, purple));
        L.obstacles.push_back(makePatrol(130, 260, 560, 260, 120, taxi));
        L.obstacles.push_back(makePatrol(480, 150, 480, 470, 90, glm::vec3(0.85f, 0.2f, 0.25f)));
        L.obstacles.push_back(makeBlock(330, 420, 34, 34));
        L.obstacles.push_back(makeBlock(330, 130, 34, 34));
        L.obstacles.push_back(makeBlock(650, 330, 34, 34));
        gLevels.push_back(L);
    }
}

// ----------------------------------------------------------------------------
// Game state
// ----------------------------------------------------------------------------
enum GameState { STATE_MENU, STATE_PLAYING, STATE_LEVEL_COMPLETE, STATE_FAILED, STATE_WON };

GameState gState = STATE_MENU;
int gLevelIndex = 0;
int gScore = 0;                     // total score (banked after every level)
int gHits = 0;                      // collisions in the current level
float gTimeLeft = 0.0f;
float gParkTimer = 0.0f;            // how long the car has been correctly parked
float gHitCooldown = 0.0f;          // stops one long crash from counting 60 times per second
float gHitFlash = 0.0f;             // car flashes red after a crash
float gCrashMsgTimer = 0.0f;
int gTimeBonus = 0, gPenalty = 0, gLevelScore = 0;     // shown on the level complete screen

Car gCar;
std::vector<Obstacle> gObstacles;

const float PARK_HOLD_TIME = 1.0f;       // stay parked and still for this long
const float PARK_MAX_SPEED = 12.0f;
const float SPOT_TOLERANCE = 3.0f;       // pixels the car may stick out of the lines
const int   BASE_SCORE = 1000, TIME_BONUS_PER_SEC = 10, HIT_PENALTY = 75;

const Level& currentLevel() { return gLevels[gLevelIndex]; }

void loadLevel(int index)
{
    gLevelIndex = index;
    const Level& L = gLevels[index];
    gObstacles = L.obstacles;
    gCar.pos = glm::vec2(L.carX, L.carY);
    gCar.angle = L.carAngle;
    gCar.speed = 0.0f;
    gCar.steer = 0.0f;
    gTimeLeft = L.timeLimit;
    gHits = 0;
    gParkTimer = 0.0f;
    gHitCooldown = 0.0f;
    gHitFlash = 0.0f;
    gCrashMsgTimer = 0.0f;
    gState = STATE_PLAYING;
}

bool carHitsSomething(const Car& c, const std::vector<Obstacle>& obstacles)
{
    Rect cr = carRect(c);
    glm::vec2 k[4];
    getCorners(cr, k);
    for (int i = 0; i < 4; i++)                       // walls of the parking lot
        if (k[i].x < PLAY_MIN_X || k[i].x > PLAY_MAX_X || k[i].y < PLAY_MIN_Y || k[i].y > PLAY_MAX_Y)
            return true;
    for (size_t i = 0; i < obstacles.size(); i++)     // cones, pillars, other cars
        if (rectsOverlap(cr, obstacles[i].r)) return true;
    return false;
}

void registerHit()
{
    if (gHitCooldown <= 0.0f)
    {
        gHits++;
        gHitCooldown = 0.6f;
        gHitFlash = 0.4f;
        gCrashMsgTimer = 1.0f;
    }
}

void updateObstacles(float dt)
{
    for (size_t i = 0; i < gObstacles.size(); i++)
    {
        Obstacle& o = gObstacles[i];
        if (!o.moving) continue;
        float len = glm::length(o.to - o.from);
        o.t += o.dir * o.speed / len * dt;
        if (o.t > 1.0f) { o.t = 1.0f; o.dir = -1; }
        if (o.t < 0.0f) { o.t = 0.0f; o.dir = 1; }
        glm::vec2 p = o.from + (o.to - o.from) * o.t;
        o.r.x = p.x;
        o.r.y = p.y;
        o.r.angle = o.baseAngle + (o.dir < 0 ? PI : 0.0f);
    }
}

// ----------------------------------------------------------------------------
// Parking check
// ----------------------------------------------------------------------------
struct ParkStatus { bool inside, aligned, slow, ok; };

ParkStatus checkParking(const Car& car, const Level& L)
{
    ParkStatus s;
    glm::vec2 k[4];
    getCorners(carRect(car), k);

    // 1) all 4 corners of the car inside the parking lines?
    float c = cosf(L.spot.angle), sn = sinf(L.spot.angle);
    s.inside = true;
    for (int i = 0; i < 4; i++)
    {
        float dx = k[i].x - L.spot.x, dy = k[i].y - L.spot.y;
        float lx = dx * c + dy * sn;         // corner position in the parking space's own axes
        float ly = -dx * sn + dy * c;
        if (fabsf(lx) > L.spot.w * 0.5f + SPOT_TOLERANCE || fabsf(ly) > L.spot.h * 0.5f + SPOT_TOLERANCE)
            s.inside = false;
    }

    // 2) car parallel to the space (front or back first is both fine)
    float d = fmodf(car.angle - L.spot.angle, PI);
    if (d > PI / 2) d -= PI;
    if (d < -PI / 2) d += PI;
    s.aligned = fabsf(d) < L.angleTolDeg * PI / 180.0f;

    // 3) nearly stopped
    s.slow = fabsf(car.speed) < PARK_MAX_SPEED;

    s.ok = s.inside && s.aligned && s.slow;
    return s;
}

// ----------------------------------------------------------------------------
// Car physics
// ----------------------------------------------------------------------------
const float CAR_ACCEL = 200.0f, CAR_BRAKE = 420.0f, CAR_REV_ACCEL = 160.0f;
const float CAR_FRICTION = 130.0f, CAR_HANDBRAKE = 650.0f;
const float CAR_MAX_FWD = 230.0f, CAR_MAX_REV = 100.0f;
const float STEER_RATE = 3.0f, STEER_RETURN = 4.5f;

float moveToward(float v, float target, float maxDelta)
{
    if (fabsf(target - v) <= maxDelta) return target;
    return v + (target > v ? maxDelta : -maxDelta);
}

// The driving keys, read once per frame (kept separate from the physics so the physics is easy to test)
struct Input { bool up, down, left, right, hand; };

Input readInput(GLFWwindow* window)
{
    Input in;
    in.up    = glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS;
    in.down  = glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS;
    in.left  = glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS;
    in.right = glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS;
    in.hand  = glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS;
    return in;
}

void updateCar(const Input& in, float dt)
{
    bool up = in.up, down = in.down, left = in.left, right = in.right, hand = in.hand;

    // steering wheel turns smoothly and returns to centre by itself
    float steerTarget = 0.0f;
    if (left)  steerTarget += MAX_STEER;
    if (right) steerTarget -= MAX_STEER;
    float rate = (steerTarget == 0.0f) ? STEER_RETURN : STEER_RATE;
    gCar.steer = moveToward(gCar.steer, steerTarget, rate * dt);

    // throttle / brake / reverse / friction
    if (up)
    {
        if (gCar.speed < -1.0f) gCar.speed += CAR_BRAKE * dt;       // moving backwards -> brake first
        else                    gCar.speed += CAR_ACCEL * dt;
    }
    else if (down)
    {
        if (gCar.speed > 1.0f)  gCar.speed -= CAR_BRAKE * dt;       // moving forwards -> brake first
        else                    gCar.speed -= CAR_REV_ACCEL * dt;
    }
    else
    {
        gCar.speed = moveToward(gCar.speed, 0.0f, CAR_FRICTION * dt);
    }
    if (hand) gCar.speed = moveToward(gCar.speed, 0.0f, CAR_HANDBRAKE * dt);
    gCar.speed = std::max(-CAR_MAX_REV, std::min(CAR_MAX_FWD, gCar.speed));

    // bicycle model: the car turns more when it moves faster, and not at all when it stands still
    Car next = gCar;
    next.angle += (gCar.speed / WHEELBASE) * tanf(gCar.steer) * dt;
    if (next.angle > 2 * PI) next.angle -= 2 * PI;
    if (next.angle < 0.0f)   next.angle += 2 * PI;
    next.pos += glm::vec2(cosf(next.angle), sinf(next.angle)) * (gCar.speed * dt);

    bool wasColliding = carHitsSomething(gCar, gObstacles);
    bool willCollide  = carHitsSomething(next, gObstacles);

    if (willCollide && !wasColliding)
    {
        // crash: stay where we were and bounce back a little
        gCar.speed = -gCar.speed * 0.3f;
        registerHit();
    }
    else
    {
        gCar = next;
        if (wasColliding) registerHit();      // hit by a moving car: let the player drive away
    }
}

void updateGame(const Input& in, float dt)
{
    if (gHitCooldown > 0.0f)    gHitCooldown -= dt;
    if (gHitFlash > 0.0f)       gHitFlash -= dt;
    if (gCrashMsgTimer > 0.0f)  gCrashMsgTimer -= dt;

    updateObstacles(dt);
    updateCar(in, dt);

    gTimeLeft -= dt;

    // parking check
    ParkStatus ps = checkParking(gCar, currentLevel());
    if (ps.ok) gParkTimer += dt; else gParkTimer = 0.0f;

    if (gParkTimer >= PARK_HOLD_TIME)
    {
        // success! calculate the score for this level
        gTimeBonus = (int)gTimeLeft * TIME_BONUS_PER_SEC;
        gPenalty = gHits * HIT_PENALTY;
        gLevelScore = std::max(100, BASE_SCORE + gTimeBonus - gPenalty);
        gScore += gLevelScore;
        std::cout << "Level " << (gLevelIndex + 1) << " complete! +" << gLevelScore
                  << "  (total " << gScore << ")" << std::endl;
        gState = (gLevelIndex + 1 >= (int)gLevels.size()) ? STATE_WON : STATE_LEVEL_COMPLETE;
    }
    else if (gTimeLeft <= 0.0f)
    {
        gTimeLeft = 0.0f;
        std::cout << "Time up on level " << (gLevelIndex + 1) << std::endl;
        gState = STATE_FAILED;
    }
}

// ----------------------------------------------------------------------------
// Rendering
// ----------------------------------------------------------------------------

// Car sprite built from simple rectangles (body, cabin, windows, lights, wheels)
void drawCarSprite(float x, float y, float ang, glm::vec3 body, float flash)
{
    drawRect(x + 3, y - 3, CAR_L + 2, CAR_W + 2, ang, 0, 0, 0, 0.30f);                 // shadow
    for (int sx = -1; sx <= 1; sx += 2)                                                  // wheels
        for (int sy = -1; sy <= 1; sy += 2)
            drawRectRel(x, y, ang, sx * 17.0f, sy * 14.5f, 12, 5, 0.05f, 0.05f, 0.05f);

    float r = body.r + (1.0f - body.r) * flash;                                          // flash red when crashed
    float g = body.g * (1.0f - flash * 0.8f);
    float b = body.b * (1.0f - flash * 0.8f);
    drawRectRel(x, y, ang, 0, 0, CAR_L, CAR_W, r, g, b);                                 // body
    drawRectRel(x, y, ang, -4, 0, 26, 22, r * 0.7f, g * 0.7f, b * 0.7f);                 // cabin / roof
    drawRectRel(x, y, ang, 10, 0, 7, 20, 0.6f, 0.85f, 1.0f);                             // windshield
    drawRectRel(x, y, ang, -18, 0, 5, 18, 0.45f, 0.65f, 0.8f);                           // rear window
    for (int s = -1; s <= 1; s += 2)
    {
        drawRectRel(x, y, ang, 27, s * 9.0f, 3, 6, 1.0f, 0.95f, 0.5f);                   // headlights
        drawRectRel(x, y, ang, -27, s * 9.0f, 3, 6, 0.9f, 0.1f, 0.1f);                   // tail lights
    }
}

void drawObstacle(const Obstacle& o)
{
    if (o.kind == OBS_CAR)
    {
        drawCarSprite(o.r.x, o.r.y, o.r.angle, o.color, 0.0f);
    }
    else if (o.kind == OBS_CONE)
    {
        drawRect(o.r.x + 2, o.r.y - 2, 18, 18, 0, 0, 0, 0, 0.3f);
        drawRect(o.r.x, o.r.y, 18, 18, 0, 1.0f, 0.5f, 0.1f);
        drawRect(o.r.x, o.r.y, 12, 12, 0, 1.0f, 1.0f, 1.0f);
        drawRect(o.r.x, o.r.y, 6, 6, 0, 1.0f, 0.5f, 0.1f);
    }
    else
    {
        drawRect(o.r.x + 3, o.r.y - 3, o.r.w, o.r.h, 0, 0, 0, 0, 0.3f);
        drawRect(o.r.x, o.r.y, o.r.w, o.r.h, 0, 0.30f, 0.30f, 0.36f);
        drawRect(o.r.x, o.r.y, o.r.w - 6, o.r.h - 6, 0, o.color.r, o.color.g, o.color.b);
    }
}

void drawParkingSpot(const Level& L, bool ok, bool inside)
{
    const Rect& s = L.spot;
    float fr = 0.2f, fg = 0.5f, fb = 0.9f, fa = 0.25f;
    if (inside) { fr = 0.3f; fg = 0.7f; fb = 1.0f; fa = 0.30f; }
    if (ok)     { fr = 0.2f; fg = 0.95f; fb = 0.3f; fa = 0.50f; }
    drawRect(s.x, s.y, s.w, s.h, s.angle, fr, fg, fb, fa);                      // coloured floor

    float t = 3.0f;                                                              // white lines
    drawRectRel(s.x, s.y, s.angle, 0,  s.h * 0.5f, s.w + t, t, 1, 1, 1, 0.9f);
    drawRectRel(s.x, s.y, s.angle, 0, -s.h * 0.5f, s.w + t, t, 1, 1, 1, 0.9f);
    drawRectRel(s.x, s.y, s.angle,  s.w * 0.5f, 0, t, s.h + t, 1, 1, 1, 0.9f);
    drawRectRel(s.x, s.y, s.angle, -s.w * 0.5f, 0, t, s.h + t, 1, 1, 1, 0.9f);
}

void drawWorld(float animTime)
{
    // asphalt + faint grid so you can feel the car moving
    for (int gx = 50; gx < 800; gx += 50) drawRect((float)gx, 275.0f, 1, 530, 0, 1, 1, 1, 0.04f);
    for (int gy = 50; gy < 540; gy += 50) drawRect(400.0f, (float)gy, 780, 1, 0, 1, 1, 1, 0.04f);

    // lot walls
    drawRect(400, 5, 800, 10, 0, 0.25f, 0.25f, 0.30f);
    drawRect(5, 300, 10, 600, 0, 0.25f, 0.25f, 0.30f);
    drawRect(795, 300, 10, 600, 0, 0.25f, 0.25f, 0.30f);
    drawRect(400, 545, 800, 10, 0, 0.25f, 0.25f, 0.30f);
    for (int i = 0; i < 40; i++)                                      // yellow warning dashes on the bottom wall
        if (i % 2 == 0) drawRect(10.0f + i * 20.0f, 5, 20, 4, 0, 0.9f, 0.75f, 0.1f);

    const Level& L = currentLevel();
    ParkStatus ps = checkParking(gCar, L);
    drawParkingSpot(L, ps.ok && gState == STATE_PLAYING, ps.inside);

    for (size_t i = 0; i < gObstacles.size(); i++) drawObstacle(gObstacles[i]);

    float flash = gHitFlash > 0.0f ? std::min(1.0f, gHitFlash / 0.2f) : 0.0f;
    drawCarSprite(gCar.pos.x, gCar.pos.y, gCar.angle, glm::vec3(0.88f, 0.15f, 0.18f), flash);

    (void)animTime;
}

void drawHUD(float animTime)
{
    const Level& L = currentLevel();
    char buf[64];

    drawRect(400, 570, 800, 60, 0, 0.07f, 0.08f, 0.11f);                      // HUD background strip

    snprintf(buf, sizeof(buf), "LEVEL %d/%d", gLevelIndex + 1, (int)gLevels.size());
    drawText(buf, 20, 574, 3, 1, 1, 1);

    int secs = (int)ceilf(gTimeLeft);
    snprintf(buf, sizeof(buf), "TIME %d", secs);
    float tr = 1, tg = 1, tb = 1;
    if (gTimeLeft < 10.0f) { tg = 0.3f; tb = 0.3f; }
    drawTextCentered(buf, 400, 574, 3, tr, tg, tb);

    snprintf(buf, sizeof(buf), "SCORE %d", gScore);
    drawTextRight(buf, 780, 574, 3, 1.0f, 0.85f, 0.25f);

    drawText(L.name, 20, 551, 2, 0.6f, 0.8f, 1.0f);

    snprintf(buf, sizeof(buf), "HITS %d", gHits);
    drawTextRight(buf, 780, 551, 2, gHits ? 1.0f : 0.7f, gHits ? 0.4f : 0.7f, gHits ? 0.4f : 0.7f);

    // timer bar
    float frac = std::max(0.0f, std::min(1.0f, gTimeLeft / L.timeLimit));
    drawRect(400, 553, 300, 8, 0, 0.2f, 0.2f, 0.25f);
    float br = frac > 0.5f ? 0.2f : 1.0f, bg = frac > 0.25f ? 0.85f : 0.25f, bb = 0.25f;
    if (frac > 0.25f && frac <= 0.5f) { br = 1.0f; bg = 0.8f; }
    drawRect(250 + 150 * frac, 553, 300 * frac, 8, 0, br, bg, bb);

    // parking progress bar (fills while the car is correctly parked and still)
    float pf = std::min(1.0f, gParkTimer / PARK_HOLD_TIME);
    if (pf > 0.0f) drawRect(250 + 150 * pf, 544, 300 * pf, 3, 0, 0.2f, 1.0f, 0.4f);

    // hint messages at the bottom of the play area
    if (gState == STATE_PLAYING)
    {
        ParkStatus ps = checkParking(gCar, L);
        if (gCrashMsgTimer > 0.0f)
        {
            snprintf(buf, sizeof(buf), "CRASH! -%d", HIT_PENALTY);
            drawTextCentered(buf, 400, 515, 4, 1.0f, 0.3f, 0.3f);
        }
        else if (ps.ok)
            drawTextCentered("HOLD STILL...", 400, 515, 3, 0.3f, 1.0f, 0.4f);
        else if (ps.inside && !ps.aligned)
            drawTextCentered("STRAIGHTEN UP", 400, 515, 3, 1.0f, 0.85f, 0.3f);
        else if (ps.inside && !ps.slow)
            drawTextCentered("SLOW DOWN", 400, 515, 3, 1.0f, 0.85f, 0.3f);
    }
    (void)animTime;
}

void drawOverlay(float animTime)
{
    bool blink = fmodf(animTime, 1.0f) < 0.65f;
    char buf[96];

    if (gState == STATE_MENU)
    {
        drawRect(400, 300, 800, 600, 0, 0.0f, 0.0f, 0.05f, 0.78f);
        drawTextCentered("SMART PARKING", 400, 440, 8, 1.0f, 0.85f, 0.25f);
        drawTextCentered("CHALLENGE", 400, 375, 8, 1.0f, 0.85f, 0.25f);
        drawTextCentered("W / UP : ACCELERATE      S / DOWN : CAR_BRAKE AND REVERSE", 400, 300, 2, 1, 1, 1);
        drawTextCentered("A / D OR LEFT / RIGHT : STEER      SPACE : CAR_HANDBRAKE", 400, 275, 2, 1, 1, 1);
        drawTextCentered("PARK INSIDE THE GLOWING BOX, STRAIGHT AND STILL", 400, 240, 2, 0.5f, 1.0f, 0.6f);
        drawTextCentered("CRASHES COST POINTS - FINISH BEFORE TIME RUNS OUT", 400, 215, 2, 1.0f, 0.5f, 0.5f);
        drawTextCentered("R : RESTART LEVEL      ESC : QUIT", 400, 180, 2, 0.7f, 0.7f, 0.75f);
        if (blink) drawTextCentered("PRESS ENTER TO START", 400, 120, 4, 1, 1, 1);
    }
    else if (gState == STATE_LEVEL_COMPLETE)
    {
        drawRect(400, 290, 800, 540, 0, 0.0f, 0.12f, 0.02f, 0.80f);
        drawTextCentered("LEVEL COMPLETE!", 400, 420, 7, 0.3f, 1.0f, 0.45f);
        snprintf(buf, sizeof(buf), "PARKING: +%d", BASE_SCORE);
        drawTextCentered(buf, 400, 340, 3, 1, 1, 1);
        snprintf(buf, sizeof(buf), "TIME BONUS: +%d", gTimeBonus);
        drawTextCentered(buf, 400, 305, 3, 1, 1, 1);
        snprintf(buf, sizeof(buf), "CRASHES (%d): -%d", gHits, gPenalty);
        drawTextCentered(buf, 400, 270, 3, 1.0f, 0.5f, 0.5f);
        snprintf(buf, sizeof(buf), "LEVEL SCORE: %d", gLevelScore);
        drawTextCentered(buf, 400, 225, 4, 1.0f, 0.85f, 0.25f);
        snprintf(buf, sizeof(buf), "TOTAL SCORE: %d", gScore);
        drawTextCentered(buf, 400, 185, 3, 1, 1, 1);
        if (blink) drawTextCentered("PRESS ENTER FOR NEXT LEVEL", 400, 120, 3, 1, 1, 1);
    }
    else if (gState == STATE_FAILED)
    {
        drawRect(400, 290, 800, 540, 0, 0.15f, 0.0f, 0.0f, 0.80f);
        drawTextCentered("TIME UP!", 400, 360, 9, 1.0f, 0.3f, 0.3f);
        drawTextCentered("YOU DID NOT PARK IN TIME", 400, 300, 3, 1, 1, 1);
        if (blink) drawTextCentered("PRESS R OR ENTER TO RETRY", 400, 220, 3, 1, 1, 1);
    }
    else if (gState == STATE_WON)
    {
        drawRect(400, 290, 800, 540, 0, 0.0f, 0.05f, 0.15f, 0.82f);
        drawTextCentered("YOU WIN!", 400, 400, 9, 1.0f, 0.85f, 0.25f);
        snprintf(buf, sizeof(buf), "ALL %d LEVELS CLEARED", (int)gLevels.size());
        drawTextCentered(buf, 400, 340, 3, 0.5f, 1.0f, 0.6f);
        snprintf(buf, sizeof(buf), "LAST LEVEL: %d", gLevelScore);
        drawTextCentered(buf, 400, 295, 3, 1, 1, 1);
        snprintf(buf, sizeof(buf), "FINAL SCORE: %d", gScore);
        drawTextCentered(buf, 400, 245, 5, 1.0f, 0.85f, 0.25f);
        if (blink) drawTextCentered("PRESS ENTER TO PLAY AGAIN", 400, 150, 3, 1, 1, 1);
    }
}

// ----------------------------------------------------------------------------
// Input that should fire once per key press (ENTER, R)
// ----------------------------------------------------------------------------
bool keyPressedOnce(GLFWwindow* window, int key)
{
    static std::map<int, bool> previous;
    bool down = glfwGetKey(window, key) == GLFW_PRESS;
    bool fired = down && !previous[key];
    previous[key] = down;
    return fired;
}

void handleStateKeys(GLFWwindow* window)
{
    bool enter   = keyPressedOnce(window, GLFW_KEY_ENTER) || keyPressedOnce(window, GLFW_KEY_KP_ENTER);
    bool restart = keyPressedOnce(window, GLFW_KEY_R);

    switch (gState)
    {
    case STATE_MENU:
        if (enter) { gScore = 0; loadLevel(0); }
        break;
    case STATE_PLAYING:
        if (restart) loadLevel(gLevelIndex);              // restart this level (score is kept)
        break;
    case STATE_LEVEL_COMPLETE:
        if (enter) loadLevel(gLevelIndex + 1);
        break;
    case STATE_FAILED:
        if (enter || restart) loadLevel(gLevelIndex);
        break;
    case STATE_WON:
        if (enter) { gScore = 0; loadLevel(0); gState = STATE_MENU; }
        break;
    }
}

int main()
{
    // glfw: initialize and configure
    // ------------------------------
    glfwInit();
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);          // fixed 800x600 so the game keeps its proportions

#ifdef __APPLE__
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
#endif

    // glfw window creation
    // --------------------
    GLFWwindow* window = glfwCreateWindow(SCR_WIDTH, SCR_HEIGHT, "Smart Parking Challenge", NULL, NULL);
    if (window == NULL)
    {
        std::cout << "Failed to create GLFW window" << std::endl;
        glfwTerminate();
        return -1;
    }
    glfwMakeContextCurrent(window);
    glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);
    glfwSwapInterval(1);                                  // vsync: same speed on every computer

    // glad: load all OpenGL function pointers
    // ---------------------------------------
    if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress))
    {
        std::cout << "Failed to initialize GLAD" << std::endl;
        return -1;
    }

    // build and compile our shader program
    // ------------------------------------
    // vertex shader
    unsigned int vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, 1, &vertexShaderSource, NULL);
    glCompileShader(vertexShader);
    // check for shader compile errors
    int success;
    char infoLog[512];
    glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &success);
    if (!success)
    {
        glGetShaderInfoLog(vertexShader, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" << infoLog << std::endl;
    }
    // fragment shader
    unsigned int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShader, 1, &fragmentShaderSource, NULL);
    glCompileShader(fragmentShader);
    // check for shader compile errors
    glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &success);
    if (!success)
    {
        glGetShaderInfoLog(fragmentShader, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::FRAGMENT::COMPILATION_FAILED\n" << infoLog << std::endl;
    }
    // link shaders
    unsigned int shaderProgram = glCreateProgram();
    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    glLinkProgram(shaderProgram);
    // check for linking errors
    glGetProgramiv(shaderProgram, GL_LINK_STATUS, &success);
    if (!success) {
        glGetProgramInfoLog(shaderProgram, 512, NULL, infoLog);
        std::cout << "ERROR::SHADER::PROGRAM::LINKING_FAILED\n" << infoLog << std::endl;
    }
    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);

    // set up vertex data (and buffer(s)) and configure vertex attributes
    // ------------------------------------------------------------------
    // ONE unit rectangle (2 triangles) centered at (0,0). The whole game is made of this shape.
    float vertices[] = {
         0.5f,  0.5f, 0.0f,  // top right
         0.5f, -0.5f, 0.0f,  // bottom right
        -0.5f, -0.5f, 0.0f,  // bottom left
         0.5f,  0.5f, 0.0f,  // top right
        -0.5f, -0.5f, 0.0f,  // bottom left
        -0.5f,  0.5f, 0.0f   // top left
    };

    unsigned int VBO, VAO;
    glGenVertexArrays(1, &VAO);
    glGenBuffers(1, &VBO);
    glBindVertexArray(VAO);

    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);

    // set the globals used by drawRect()
    gShaderProgram = shaderProgram;
    gVAO = VAO;
    gTransformLoc = glGetUniformLocation(shaderProgram, "transform");
    gColorLoc = glGetUniformLocation(shaderProgram, "ourColor");
    // orthographic projection: lets us use pixel coordinates (0..800, 0..600) instead of -1..1
    gProjection = glm::ortho(0.0f, (float)SCR_WIDTH, 0.0f, (float)SCR_HEIGHT);

    glEnable(GL_BLEND);                                   // needed for transparent shadows / overlays
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    // game setup
    buildLevels();
    loadLevel(0);
    gState = STATE_MENU;                                  // level 1 is shown behind the start menu

    double lastTime = glfwGetTime();
    double titleTimer = 0.0;

    // render loop
    // -----------
    while (!glfwWindowShouldClose(window))
    {
        double now = glfwGetTime();
        float dt = (float)(now - lastTime);
        lastTime = now;
        if (dt > 0.033f) dt = 0.033f;                     // avoid huge jumps if the window freezes for a moment
        float animTime = (float)now;

        // input
        // -----
        processInput(window);
        handleStateKeys(window);

        // update
        // ------
        if (gState == STATE_PLAYING) updateGame(readInput(window), dt);
        else if (gState == STATE_MENU) updateObstacles(dt);

        // render
        // ------
        glClearColor(0.16f, 0.17f, 0.20f, 1.0f);          // asphalt grey
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(shaderProgram);
        glBindVertexArray(VAO);

        drawWorld(animTime);
        drawHUD(animTime);
        drawOverlay(animTime);

        // show live info in the window title too
        titleTimer += dt;
        if (titleTimer > 0.25)
        {
            titleTimer = 0.0;
            char title[160];
            if (gState == STATE_PLAYING)
                snprintf(title, sizeof(title), "Smart Parking Challenge | Level %d/%d | Time %d | Score %d | Hits %d",
                         gLevelIndex + 1, (int)gLevels.size(), (int)ceilf(gTimeLeft), gScore, gHits);
            else
                snprintf(title, sizeof(title), "Smart Parking Challenge | Score %d", gScore);
            glfwSetWindowTitle(window, title);
        }

        // glfw: swap buffers and poll IO events (keys pressed/released, mouse moved etc.)
        // -------------------------------------------------------------------------------
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    // optional: de-allocate all resources once they've outlived their purpose:
    // ------------------------------------------------------------------------
    glDeleteVertexArrays(1, &VAO);
    glDeleteBuffers(1, &VBO);
    glDeleteProgram(shaderProgram);

    // glfw: terminate, clearing all previously allocated GLFW resources.
    // ------------------------------------------------------------------
    glfwTerminate();
    return 0;
}

// process all input: query GLFW whether relevant keys are pressed/released this frame and react accordingly
// ---------------------------------------------------------------------------------------------------------
void processInput(GLFWwindow *window)
{
    if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
        glfwSetWindowShouldClose(window, true);
}

// glfw: whenever the window size changed (by OS or user resize) this callback function executes
// ---------------------------------------------------------------------------------------------
void framebuffer_size_callback(GLFWwindow* window, int width, int height)
{
    // make sure the viewport matches the new window dimensions; note that width and
    // height will be significantly larger than specified on retina displays.
    glViewport(0, 0, width, height);
}
