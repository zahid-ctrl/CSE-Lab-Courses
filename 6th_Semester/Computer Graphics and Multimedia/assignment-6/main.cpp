#include "glad.h"
#include "glfw3.h"

#include "glm/glm/glm.hpp"
#include "glm/glm/gtc/matrix_transform.hpp"
#include "glm/glm/gtc/type_ptr.hpp"

#include <iostream>
#include <cmath>

const char* vertexShaderSource = R"(
#version 330 core
layout (location = 0) in vec3 aPos;

uniform mat4 transform;

void main()
{
    gl_Position = transform * vec4(aPos, 1.0);
}
)";

const char* fragmentShaderSource = R"(
#version 330 core
out vec4 FragColor;

uniform vec3 rectColor;

void main()
{
    FragColor = vec4(rectColor, 1.0);
}
)";

void framebuffer_size_callback(GLFWwindow* window,
                               int width, int height);

void processInput(GLFWwindow* window);

const unsigned int SCR_WIDTH = 800;
const unsigned int SCR_HEIGHT = 600;

int main()
{
    // Initialize GLFW
    if (!glfwInit())
    {
        std::cout << "Failed to initialize GLFW\n";
        return -1;
    }

    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE,
                   GLFW_OPENGL_CORE_PROFILE);

    GLFWwindow* window = glfwCreateWindow(
        SCR_WIDTH, SCR_HEIGHT,
        "Animated Rectangle", NULL, NULL);

    if (window == NULL)
    {
        std::cout << "Failed to create GLFW window\n";
        glfwTerminate();
        return -1;
    }

    glfwMakeContextCurrent(window);

    glfwSetFramebufferSizeCallback(
        window, framebuffer_size_callback);

    // Initialize GLAD
    if (!gladLoadGLLoader(
        (GLADloadproc)glfwGetProcAddress))
    {
        std::cout << "Failed to initialize GLAD\n";
        glfwTerminate();
        return -1;
    }

    // Rectangle vertices
    float vertices[] = {
         0.5f,  0.5f, 0.0f,
         0.5f, -0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,

         0.5f,  0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        -0.5f,  0.5f, 0.0f
    };

    // Vertex shader
    unsigned int vertexShader =
        glCreateShader(GL_VERTEX_SHADER);

    glShaderSource(vertexShader, 1,
                   &vertexShaderSource, NULL);

    glCompileShader(vertexShader);

    // Fragment shader
    unsigned int fragmentShader =
        glCreateShader(GL_FRAGMENT_SHADER);

    glShaderSource(fragmentShader, 1,
                   &fragmentShaderSource, NULL);

    glCompileShader(fragmentShader);

    // Shader program
    unsigned int shaderProgram =
        glCreateProgram();

    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);

    glLinkProgram(shaderProgram);

    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);

    // VAO and VBO
    unsigned int VAO, VBO;

    glGenVertexArrays(1, &VAO);
    glGenBuffers(1, &VBO);

    glBindVertexArray(VAO);

    glBindBuffer(GL_ARRAY_BUFFER, VBO);

    glBufferData(GL_ARRAY_BUFFER,
                 sizeof(vertices), vertices,
                 GL_STATIC_DRAW);

    glVertexAttribPointer(
        0, 3, GL_FLOAT, GL_FALSE,
        3 * sizeof(float), (void*)0);

    glEnableVertexAttribArray(0);

    // Uniform locations
    int transformLoc =
        glGetUniformLocation(shaderProgram, "transform");

    int colorLoc =
        glGetUniformLocation(shaderProgram, "rectColor");

    // Animation loop
    while (!glfwWindowShouldClose(window))
    {
        processInput(window);

        float t = (float)glfwGetTime();

        glClearColor(0.12f, 0.12f, 0.12f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(shaderProgram);

        // 1. Gradually change color: RED <-> WHITE
        float colorMix =
            (sinf(t * 1.0f) + 1.0f) / 2.0f;

        glm::vec3 red(1.0f, 0.0f, 0.0f);
        glm::vec3 white(1.0f, 1.0f, 1.0f);

        glm::vec3 currentColor =
            glm::mix(red, white, colorMix);

        glUniform3fv(colorLoc, 1,
                     glm::value_ptr(currentColor));

        // 2. Magnification and shrinking
        float magnification =
            0.85f + 0.30f *
            (sinf(t * 1.2f) + 1.0f) / 2.0f;

        // 3. Squeezing and stretching
        float squeezeX =
            0.65f + 0.35f *
            (sinf(t * 2.0f) + 1.0f) / 2.0f;

        float squeezeY =
            1.0f - 0.35f *
            (sinf(t * 2.0f) + 1.0f) / 2.0f;

        float scaleX = magnification * squeezeX;
        float scaleY = magnification * squeezeY;

        // 4. Translation along X and Y
        float xOffset = 0.35f * sinf(t * 0.7f);
        float yOffset = 0.25f * cosf(t * 0.9f);

        // Apply transformations
        glm::mat4 transform(1.0f);

        transform = glm::translate(
            transform,
            glm::vec3(xOffset, yOffset, 0.0f));

        transform = glm::scale(
            transform,
            glm::vec3(scaleX, scaleY, 1.0f));

        glUniformMatrix4fv(
            transformLoc, 1, GL_FALSE,
            glm::value_ptr(transform));

        // Draw rectangle
        glBindVertexArray(VAO);

        glDrawArrays(GL_TRIANGLES, 0, 6);

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    // Clean up
    glDeleteVertexArrays(1, &VAO);
    glDeleteBuffers(1, &VBO);
    glDeleteProgram(shaderProgram);

    glfwTerminate();

    return 0;
}

// Keyboard input
void processInput(GLFWwindow* window)
{
    if (glfwGetKey(window, GLFW_KEY_ESCAPE)
        == GLFW_PRESS)
    {
        glfwSetWindowShouldClose(window, true);
    }
}

// Window resize
void framebuffer_size_callback(
    GLFWwindow* window, int width, int height)
{
    glViewport(0, 0, width, height);
}