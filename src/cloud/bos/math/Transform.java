package cloud.bos.math;

import static cloud.bos.math.Mathf.cos;
import static cloud.bos.math.Mathf.sin;

public class Transform {

    public static Matrix4f identityMatrix(){
        return new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
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
                {v.getX(), 0,        0,        0},
                {0,        v.getY(), 0,        0},
                {0,        0,        v.getZ(), 0},
                {0,        0,        0,        1}
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
                {cos(-v.getY()),  0,               sin(-v.getY()),  0},
                {0,               1,               0,               0},
                {-sin(-v.getY()), 0,               cos(-v.getY()),  0},
                {0,               0,               0,               1}
        });

        Matrix4f z = new Matrix4f(new float[][]{
                {cos(-v.getZ()), -sin(-v.getZ()),  0,               0},
                {sin(-v.getZ()),  cos(-v.getZ()),  0,               0},
                {0,               0,               1,               0},
                {0,               0,               0,               1}
        });
        return x.mul(y).mul(z);
    }
}
