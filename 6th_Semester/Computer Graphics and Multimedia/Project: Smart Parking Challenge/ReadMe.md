# Smart Parking Challenge

An interactive 2D parking simulation game written in **C++ and OpenGL 3.3** (GLFW + GLAD + GLM).
Drive the car into the glowing parking space without hitting anything, before time runs out.

Built on the Day 7 class code (same GLFW/GLAD setup, same inline shaders, `uniform vec4 ourColor`
for colours, `uniform mat4 transform` + GLM for moving/rotating/scaling, same `processInput()`).
The whole game is drawn with **one rectangle** (2 triangles) reused with different transforms/colours.

## Controls

| Key | Action |
|---|---|
| `W` / `Up` | Accelerate (brakes first if you are reversing) |
| `S` / `Down` | Brake, then reverse |
| `A` / `Left`, `D` / `Right` | Steer (car only turns while moving) |
| `Space` | Handbrake |
| `R` | Restart current level |
| `Enter` | Start / next level / retry |
| `Esc` | Quit |

## How to win a level

Park so that **all four corners of the car are inside the parking lines**, the car is **parallel to the space**
(front or back first is fine) and **almost stopped**. Stay like that for **1 second** (green progress bar under the timer).

## Scoring

* Parking: **+1000**
* Time bonus: **+10 per second left**
* Each crash: **-75** (a long push against an obstacle counts again every 0.6 s)
* Minimum 100 points per level. Total score is kept across levels; `R` restarts a level without losing points.
* Run out of time -> level failed, press `R`/`Enter` to retry.

## Levels

| # | Name | New challenge | Time |
|---|---|---|---|
| 1 | Warm Up | Big space, a few cones | 60 s |
| 2 | Between Cars | Perpendicular space between parked cars | 60 s |
| 3 | Tight Squeeze | Narrow space + pillars to weave through | 55 s |
| 4 | Parallel Park | Parallel parking against the wall | 55 s |
| 5 | Rush Hour | Two moving cars patrol the lot, tightest alignment | 60 s |

## Code map (`src/main.cpp`)

* **Shaders / `drawRect()`** - the single rectangle + `ourColor` and `transform` uniforms (GLM translate/rotate/scale + `glm::ortho` so we can use pixel coordinates).
* **5x7 pixel font** - letters/numbers drawn from small rectangles (no font library needed).
* **Collision detection** - `rectsOverlap()` uses the Separating Axis Theorem for rotated rectangles; `carHitsSomething()` also checks the lot walls.
* **Car physics** - `updateCar()`: acceleration, braking, friction, handbrake and a bicycle steering model (`angle += speed / wheelbase * tan(steer) * dt`).
* **Levels** - `buildLevels()` lists the obstacles, spot, start position, time limit and angle tolerance of every level.
* **Game states** - menu -> playing -> level complete / failed -> win, handled in `handleStateKeys()`.
* **HUD** - level, timer + bar, score, hits, hints ("SLOW DOWN", "STRAIGHTEN UP", "HOLD STILL...").

## Setup (same as class)

### Windows
* `glfw3.dll` is already in **build** and **lib**.
* Install MSYS2 + `pacman -S base-devel gcc`, add `C:\msys64\usr\bin` to PATH.
* Run `make win` in the project folder. The `.exe` is created in **build**.
* Needs OpenGL 3.3 or higher.

### Linux
* `sudo apt-get install libglfw3-dev`
* Run `make linux`. The executable is created in **build**.
