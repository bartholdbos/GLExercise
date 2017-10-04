package cloud.bos.math;

import static cloud.bos.math.Mathf.cos;
import static cloud.bos.math.Mathf.sin;
import static cloud.bos.math.Mathf.sq;

public class Matrix4f {

    private float a[][];

    public Matrix4f(){
        this.a = new float[4][4];
    }

    public Matrix4f(float a[][]){
        this.a = a;
    }

    public static Matrix4f translateMatrix(Vector3f v){
        return new Matrix4f(new float[][]{
                {1, 0, 0, v.getX()},
                {0, 1, 0, v.getY()},
                {0, 0, 1, v.getZ()},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4f scaleMatrix(Vector3f v){
        return new Matrix4f(new float[][]{
                {v.getX(), 0, 0, 0},
                {0, v.getY(), 0, 0},
                {0, 0, v.getZ(), 0},
                {0, 0, 0, 1}
        });
    }


    public static Matrix4f rotationMatrix(Vector3f v){
        Matrix4f x = new Matrix4f(new float[][]{
                {1,               0,               0,               0},
                {0,               cos(-v.getX()), -sin(-v.getX()),  0},
                {0,               sin(-v.getX()),  cos(-v.getX()),  0},
                {0,               0,               0,               1}
        });

        Matrix4f y = new Matrix4f(new float[][]{
                {cos(-v.getY()),  0,              -sin(-v.getY()),  0},
                {0,               1,               0,               0},
                {sin(-v.getY()),  0,               cos(-v.getY()),  0},
                {0,               0,               0,               1}
        });

        Matrix4f z = new Matrix4f(new float[][]{
                {cos(-v.getZ()), -sin(-v.getZ()),  0,               0},
                {sin(-v.getZ()),  cos(v.getZ()),   0,               0},
                {0,               0,               1,               0},
                {0,               0,               0,               1}
        });
        return z.mul(y).mul(x);
    }

    public Matrix4f mul(Matrix4f b){
        Matrix4f res = new Matrix4f();

        for (int r = 0; r < 4; r++){
            for (int c = 0; c < 4; c++){
                res.a[r][c] = a[r][0] * b.a[0][c] +
                              a[r][1] * b.a[1][c] +
                              a[r][2] * b.a[2][c] +
                              a[r][3] * b.a[3][c];
            }
        }

        return res;
    }

    public float[][] getMatrix() {
        return a;
    }

    @Override
    public String toString() {
        String lines = "";

        for (float[] r : a){
            lines += "[";
            lines += String.format("%1$.0f %2$.0f %3$.0f %4$.0f", r[0], r[1], r[2], r[3]);
            lines += "]\n";
        }

        return  lines;
    }
}
