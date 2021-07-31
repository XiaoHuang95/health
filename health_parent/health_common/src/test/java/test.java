import com.blank.utils.QiniuUtils;
import com.blank.utils.SMSUtils;
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
    @Test
    public void send() throws Exception {
        String[] params = {"弟弟"};
        SMSUtils.sendShortMessage("1057546","17673200384",params);
    }
}