package cloud.bos;

import cloud.bos.math.Mathf;
import cloud.bos.math.Matrix4f;
import cloud.bos.math.Transform;
import cloud.bos.math.Vector3f;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec;
import com.hackoeur.jglm.Vec4;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import static cloud.bos.math.Mathf.toRadians;
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
    private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector3f rotation = new Vector3f();

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
        glEnable(GL_DEPTH_TEST);
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

        float vertices[] = {
                -0.5f, -0.5f, -0.5f,  1.0f, 1.0f, 1.0f, //FRONT WHITE
                 0.5f, -0.5f, -0.5f,  1.0f, 1.0f, 1.0f,
                 0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 1.0f,
                 0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  1.0f, 1.0f, 1.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f, //BACK GREEN
                 0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f, //TOP BLUE
                -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f,

                 0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.5f, //SIDE GRAY
                 0.5f,  0.5f, -0.5f,  0.5f, 0.5f, 0.5f,
                 0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.5f,
                 0.5f, -0.5f, -0.5f,  0.5f, 0.5f, 0.5f,
                 0.5f, -0.5f,  0.5f,  0.5f, 0.5f, 0.5f,
                 0.5f,  0.5f,  0.5f,  0.5f, 0.5f, 0.5f,

                -0.5f, -0.5f, -0.5f,  1.0f, 1.0f, 0.0f, //BOTTOM YELLOW
                 0.5f, -0.5f, -0.5f,  1.0f, 1.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  1.0f, 1.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  1.0f, 1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  1.0f, 1.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  1.0f, 1.0f, 0.0f,

                -0.5f,  0.5f, -0.5f,  1.0f, 0.0f, 0.0f, //SIDE RED
                 0.5f,  0.5f, -0.5f,  1.0f, 0.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 0.0f, 0.0f
        };

        Vector3f[] cubePositions = {
                new Vector3f( 0.0f,  0.0f,  0.0f),
                new Vector3f( 2.0f,  5.0f, -15.0f),
                new Vector3f(-1.5f, -2.2f, -2.5f),
                new Vector3f(-3.8f, -2.0f, -12.3f),
                new Vector3f( 2.4f, -0.4f, -3.5f),
                new Vector3f(-1.7f,  3.0f, -7.5f),
                new Vector3f( 1.3f, -2.0f, -2.5f),
                new Vector3f( 1.5f,  2.0f, -2.5f),
                new Vector3f( 1.5f,  0.2f, -1.5f),
                new Vector3f(-1.3f,  1.0f, -1.5f),
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
        glfwSetCursorPos(window, 800 / 2, 600 / 2);
        while (!glfwWindowShouldClose(window)) {
            processInput(window);

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shaderProgram.bind();

            Matrix4f view = Transform.identityMatrix();
            System.out.println(position.getX() + ", " + position.getY() + ", " + position.getZ());
            view.mul(Transform.translateMatrix(new Vector3f(-position.getX(), position.getZ(), position.getY())));
            shaderProgram.setMatrix4f("view", view);

            Mat4 proj = Matrices.perspective((float) (toRadians(180.0f)), 800/600, 0.1f, 100.0f);
            float[][] test = new float[4][4];
            for(int i = 0; i < 4; i++){
                for (int j = 0; j < 4; j++){
                    test[j][i] = proj.getBuffer().get(j+i*4);
                }
            }

            Matrix4f projection = new Matrix4f(test);

            Matrix4f exprojection = new Matrix4f(new float[][]{
                    {1.0f, 0.0f, 0.0f, 0.0f},
                    {0.0f, 1.0f, 0.0f, 0.0f},
                    {0.0f, 0.0f, 1.0f, 0.0f},
                    {0.0f, 0.0f, 0.0f, 1.0f}
            });

            shaderProgram.setMatrix4f("projection", exprojection);

            glBindVertexArray(VAO);
            for (int i = 0; i < 10; i++){
                Matrix4f model = Transform.identityMatrix();
                model.mul(Transform.translateMatrix(cubePositions[i]));

                float angle = 20.0f * i;
                model.mul(Transform.rotationMatrix(new Vector3f(toRadians(angle), toRadians(angle) * 0.3f, toRadians(angle) *0.5f)));

                shaderProgram.setMatrix4f("model", model);

                glDrawArrays(GL_TRIANGLES, 0, 36);
            }

            shaderProgram.unbind();

            /**
             * OLDSTUFF
             */

//            shaderProgram.bind();
//
//            Matrix4f transtest = Transform.identityMatrix();
//
//            float scaler = (Mathf.sin((float) glfwGetTime()) / 4.0f) + 0.75f;
//            Vector3f scale= new Vector3f(scaler, scaler, scaler);
//            //transtest.mul(Transform.scaleMatrix(scale));
//
//            //System.out.println(transtest);
//
//            float scaler1 = (float) glfwGetTime() / 2 % 2 * (float) PI;
//            transtest.mul(Transform.translateMatrix(new Vector3f(0.5f, -0.5f, 0.0f)));
//            transtest.mul(Transform.rotationMatrix(new Vector3f(0.0f, scaler1, 0.0f)));
//
//            shaderProgram.setMatrix4f("transform", transtest);
//
//            Matrix4f projectionMatrix = Transform.identityMatrix();
//
//
//            Matrix4f viewMatrix = look(position, rotation);
//
//
//            Matrix4f modelMatrix = Transform.identityMatrix();
//            modelMatrix.mul(Transform.translateMatrix(new Vector3f(0.5f, 0.0f, 0.0f)));
//
//
//            shaderProgram.setMatrix4f("view", projectionMatrix.mul(viewMatrix).mul(modelMatrix));
//
//            glBindVertexArray(VAO);
//            glDrawArrays(GL_TRIANGLES, 0, 36);
//
//            shaderProgram.unbind();

            /**
             * OLDSTUFF
             */

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        shaderProgram.delete();
    }

    private Matrix4f look(Vector3f position, Vector3f rotation){
        Matrix4f viewMatrix = Transform.identityMatrix();

        viewMatrix.mul(Transform.translateMatrix(new Vector3f(
                -position.getX(),
                -position.getY(),
                -position.getZ()
        )));

        viewMatrix.mul(Transform.rotationMatrix(new Vector3f(
                rotation.getX(),
                rotation.getY(),
                0.0f
        )));

        //            viewMatrix.mul(Transform.translateMatrix(new Vector3f(
//                    1.0f,
//                    0.0f,
//                    0.0f
//            )));
//            viewMatrix.mul(Transform.rotationMatrix(new Vector3f(
//                    (float) (0.0f * PI),
//                    (float) (0.2f * PI),
//                    (float) (0.0f * PI)
//            )));

        return viewMatrix;
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

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS){
            position.setY(position.getY() + 0.5f);
        }

        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS){
            position.setY(position.getY() - 0.5f);
        }

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS){
            position.setX(position.getX() - 0.1f);
        }

        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS){
            position.setX(position.getX() + 0.1f);
        }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS){
            position.setZ(position.getZ() + 0.5f);
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS){
            position.setZ(position.getZ() - 0.5f);
        }

        double[] xpos = new double[1];
        double[] ypos = new double[1];
        glfwGetCursorPos(window, xpos, ypos);
        //glfwSetCursorPos(window, 800 / 2, 600 / 2);

//        rotation.setX(rotation.getX() + (float) (800 / 2 - xpos[0]) / 0.02f);
//        rotation.setY(rotation.getY() + (float) (600 / 2 - ypos[0]) / 0.02f);
    }

    public static void main(String[] args) {
        new Main();
    }
}