package cloud.bos.math;

public class Vector3f {

    private float x;
    private float y;
    private float z;

    public Vector3f(){
        this(0, 0, 0);
    }

    public Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f imul(Matrix4f matrix){
        Vector3f res = new Vector3f();
        float[][] a = matrix.getMatrix();

        res.setX(a[0][0] * x + a[0][1] * y + a[0][2] * z + a[0][3]);
        res.setY(a[1][0] * x + a[1][1] * y + a[1][2] * z + a[1][3]);
        res.setZ(a[2][0] * x + a[2][1] * y + a[2][2] * z + a[2][3]);

        return res;
    }

    public Vector3f mul(Matrix4f matrix){
        float[][] a = matrix.getMatrix();

        float newx = a[0][0] * x + a[0][1] * y + a[0][2] * z + a[0][3];
        float newy = a[1][0] * x + a[1][1] * y + a[1][2] * z + a[1][3];
        float newz = a[2][0] * x + a[2][1] * y + a[2][2] * z + a[2][3];

        this.x = newx;
        this.y = newy;
        this.z = newz;
        return this;
    }


    public float rotate(){
        return 0;
    }

    public float getLenght(){
        return 0;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("[%1$.0f %2$.0f %3$.0f]", x, y, z);
    }
}
