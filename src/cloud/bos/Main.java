package cloud.bos;

import cloud.bos.math.Mathf;
import cloud.bos.math.Matrix4f;
import cloud.bos.math.Transform;
import cloud.bos.math.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import static java.lang.Math.PI;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private long window;

    public Main() {
        init();
        loop();
        destroy();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 600, "GLExercise", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create GLFW window");

        glfwMakeContextCurrent(window);

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            glViewport(0, 0, width, height);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pWidth.get(0)) / 2
            );
        }


        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
    }

    private void loop() {
        ShaderProgram shaderProgram = new ShaderProgram();
        try {
            shaderProgram.loadShader(new File("shaders/shader.vert"), GL_VERTEX_SHADER);
            shaderProgram.loadShader(new File("shaders/shader.frag"), GL_FRAGMENT_SHADER);
            shaderProgram.link();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Vector3f bottomright = new Vector3f(0.5f, -0.5f, 0.0f);
        Vector3f bottomleft = new Vector3f(-0.5f, -0.5f, 0.0f);
        Vector3f top = new Vector3f(0.0f,  0.5f, 0.0f);

        float vertices[] = {
                bottomright.getX(), bottomright.getY(), bottomright.getZ(), 1.0f, 0.0f, 0.0f,   // bottom right
                bottomleft.getX(),  bottomleft.getY(),  bottomleft.getZ(),  0.0f, 1.0f, 0.0f,   // bottom left
                top.getX(),         top.getY(),         top.getZ(),         0.0f, 0.0f, 1.0f    // top
        };

//        int indices[] = { // EBO
//                0, 1, 3,
//                1, 2, 3
//        };

        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
//        int EBO = glGenBuffers(); // EBO
        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO); // EBO
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW); // EBO

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.SIZE / Byte.SIZE, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.SIZE / Byte.SIZE, 3 * Float.SIZE / Byte.SIZE);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); // EBO

//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); // EBO
        while (!glfwWindowShouldClose(window)) {
            processInput(window);

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shaderProgram.bind();

            Matrix4f transtest = Transform.identityMatrix();

            float scaler = (Mathf.sin((float) glfwGetTime()) / 4.0f) + 0.75f;
            Vector3f scale= new Vector3f(scaler, scaler, scaler);
            //transtest.mul(Transform.scaleMatrix(scale));

            //System.out.println(transtest);

            float scaler1 = (float) glfwGetTime() / 2 % 2 * (float) PI;
//            transtest.mul(Transform.translateMatrix(new Vector3f(0.5f, -0.5f, 0.0f)));
//            transtest.mul(Transform.rotationMatrix(new Vector3f(0.0f, scaler1, 0.0f)));

            shaderProgram.setMatrix4f("transform", transtest);

            Matrix4f viewmatrix = Transform.identityMatrix();
            viewmatrix.mul(Transform.rotationMatrix(new Vector3f(0.3f * (float) PI, 0.0f * (float) PI, 0.0f * (float) PI)));
            shaderProgram.setMatrix4f("view", viewmatrix);

            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES, 0, 3);

            shaderProgram.unbind();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        shaderProgram.delete();
    }

    private void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void processInput(long window) {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}