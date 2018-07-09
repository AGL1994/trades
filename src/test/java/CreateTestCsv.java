import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateTestCsv {
    public static void main(String[] args) throws IOException {
        File file = new File("e:/2017-07-10.csv");
        if(!file.isFile()){
            file.createNewFile();
        }

        PrintWriter pw = new PrintWriter(file);
//        for(int i= 0; i < 20000000; i++){
//            String str = "ITEM_NO"+ i + "," + i + "," + i + "," + i;
//            pw.println(str);
//        }

        for(int i= 0; i < 200000; i++){
            // 插入少量错误数据
            String str = "";
            if(i%80000 == 0){
                str = "ITEM_NO"+ i + "," + i + "," + "error" + "," + i;  // 读取失败记录
            }else{

//                if(i == 10090 || i == 98762){
//                    str = "ITEM_NO"+ i + "," + "1234567890123" + "," + i + "," + i; // 插入失败记录
//                    System.out.println(str);
//                }else{
                    str = "ITEM_NO"+ i + "," + i + "," + i + "," + i;
//                }
            }

            pw.println(str);
        }

        pw.flush();
        pw.close();
    }
}
