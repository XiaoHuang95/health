import com.blank.utils.QiniuUtils;
import org.junit.Test;

public class test{
    @Test
    public void test(){
        QiniuUtils.upload2Qiniu("D:\\info\\ssm项目\\health\\health_parent\\health_common\\src\\main\\resources\\my.jpg","my.jpg");
    }
    @Test
    public void delete(){
        QiniuUtils.deleteFileFromQiniu("my.jpg");
    }
}