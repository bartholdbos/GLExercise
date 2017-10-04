package cloud.bos.math;

public class Matrix4f {

    private float a[][];

    public Matrix4f(){
        this.a = new float[4][4];
    }

    public Matrix4f(float a[][]){
        this.a = a;
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
