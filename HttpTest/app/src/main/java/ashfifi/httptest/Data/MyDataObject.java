package ashfifi.httptest.Data;

/**
 * Created by aa on 17/8/8.
 */

public class MyDataObject {
    public  int x,y;
    public MyDataObject(int x,int y){
        this.x=x;
        this.y=y;
    }
    public void setValueX(int x){
         this.x=x;
    }
    public void setValueY(int y){
         this.y=y;
    }
    public int getValueX(){
        return x;
    }
    public int getValueY(){
        return y;
    }
}
