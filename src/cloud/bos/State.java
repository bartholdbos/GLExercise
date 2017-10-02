package cloud.bos;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

public class State {

    private int vboVertexHandle;

    public void init() {
//        float vertices[] = {
//                -0.5f, -0.5f, 0.0f,
//                0.5f, -0.5f, 0.0f,
//                0.0f, 0.5f, 0.0f
//        };

//        glMatrixMode(GL_PROJECTION);
//        glLoadIdentity();
//        glOrtho(1, 1, 1, 1, 1, -1);
//        glMatrixMode(GL_MODELVIEW);
//        glLoadIdentity();
//
//        final int amountOfVertices = 3;
//        final int vertexSize = 3;
//        final int colorSize = 3;
//
//        FloatBuffer vertexData = BufferUtils.createFloatBuffer(amountOfVertices * vertexSize);
//        vertexData.put(new float[]{
//                -0.5f, -0.5f, 0.0f,
//                0.5f, -0.5f, 0.0f,
//                0.0f, 0.5f, 0.0f
//        });
//        vertexData.flip();
//
//        vboVertexHandle = glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
//        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
//        glBindBuffer(GL_ARRAY_BUFFER, 0);

//        int vao = glGenVertexArrays();
//        glBindVertexArray(vao);

//        MemoryStack stack = MemoryStack.stackPush();
//        FloatBuffer vertices = stack.mallocFloat(3 * 6);
//        vertices.put(-0.6f).put(-0.4f).put(0f).put(1f).put(0f).put(0f);
//        vertices.put(0.6f).put(-0.4f).put(0f).put(0f).put(1f).put(0f);
//        vertices.put(0f).put(0.6f).put(0f).put(0f).put(0f).put(1f);
//        vertices.flip();
//
//        int vbo = glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vbo);
//        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
//        MemoryStack.stackPop();
        FloatBuffer buffer = memAllocFloat(3 * 2);
        buffer.put(-0.5f).put(-0.5f);
        buffer.put(+0.5f).put(-0.5f);
        buffer.put(+0.0f).put(+0.5f);
        buffer.flip();

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        memFree(buffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 0, 0L);

//        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
//        glShaderSource(vertexShader, "#version 150 core\n" +
//                "\n" +
//                "in vec3 position;\n" +
//                "in vec3 color;\n" +
//                "\n" +
//                "out vec3 vertexColor;\n" +
//                "\n" +
//                "uniform mat4 model;\n" +
//                "uniform mat4 view;\n" +
//                "uniform mat4 projection;\n" +
//                "\n" +
//                "void main() {\n" +
//                "    vertexColor = color;\n" +
//                "    mat4 mvp = projection * view * model;\n" +
//                "    gl_Position = mvp * vec4(position, 1.0);\n" +
//                "}");
//        glCompileShader(vertexShader);
//
//        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
//        glShaderSource(fragmentShader, "#version 150 core\n" +
//                "\n" +
//                "in vec3 vertexColor;\n" +
//                "\n" +
//                "out vec4 fragColor;\n" +
//                "\n" +
//                "void main() {\n" +
//                "    fragColor = vec4(vertexColor, 1.0);\n" +
//                "}");
//        glCompileShader(fragmentShader);
//
//        int status = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
//        if (status != GL_TRUE) {
//            throw new RuntimeException(glGetShaderInfoLog(vertexShader));
//        }
//
//        int status1 = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
//        if (status1 != GL_TRUE) {
//            throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
//        }
//
//        int shaderProgram = glCreateProgram();
//        glAttachShader(shaderProgram, vertexShader);
//        glAttachShader(shaderProgram, fragmentShader);
//        glBindFragDataLocation(shaderProgram, 0, "fragColor");
//        glLinkProgram(shaderProgram);
//
//        int status2 = glGetProgrami(shaderProgram, GL_LINK_STATUS);
//        if (status2 != GL_TRUE) {
//            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
//        }
//
//        glUseProgram(shaderProgram);
    }

    public void draw() {
//        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
//        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
