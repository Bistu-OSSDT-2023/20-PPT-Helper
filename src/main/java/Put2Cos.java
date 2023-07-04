import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;

import java.io.File;
import java.time.Instant;
import java.util.concurrent.ExecutorService;

public class Put2Cos {
    // 创建 COSClient 实例，这个实例用来后续调用请求
    COSClient cosClient;
    COSClient createCOSClient() {
        // 这里需要已经获取到临时密钥的结果。
        // 临时密钥的生成参见 https://cloud.tencent.com/document/product/436/14048#cos-sts-sdk
        String tmpSecretId = "AKIDxtlXksCATQt8ke03yxktIRS9RkIO9w7XaoMI6EdniPH5lANEppIvZr7nPfVFa2QL";
        String tmpSecretKey = "WJbz2PJGINWWAPgTKI6PgZPe+wqJLHoeGucagngmQUo=";
        String sessionToken = "0mPmSkvHd48ByFqnLDh7qC0ADEEw66maac0fee1bbc1f2e60e2d8858bd0e478c5xtDJSGkwqe_uA-YxRxhBY2AHUzIibPpBKnWHMtCgvs9PuAXGmJHp3vK-_GLztko9GsX_FwY3QFoxaxlXQhLFNsE15q-IgL7-_jUV-RnojPDQB1dFBWgSSm0cU7d7W69CnrpB3VT-AimIEH1oZORmf7HnooYLlgWvMYvJjOPfY2lgoXmYFHi-FzldGsOmLul1CtTQpmfxfGnX-ZY0rFE2oQo5M0acTIIiAI5hMDfR2GYyh5RDApwS-W4PC8rWaP9Rw-zyLBuaBIZn-9salEx6tHWq5zFb4CByJ5XU9VAHuA0_vudfcSC1nR4rZA_FRjOlVnLIu2jWKAqZQoTSUdJKoTxua890ZbMNLbeyIDudMMVV0gO90aN0gxduW_qsL9RFMzia15LiUb4QOO0x7_pn4v-qNhx6cXCN2xak8AhsZREbA54EY73RiEoh37EKkO8VGVC5_13BjVCsZVtrdQR9TWAsMDMyK2Crf7oL-Dd_lGaRFV6HrglvsfHl8hvOchiPLeNsiGnB8iHOvp4h606owpbypGonEsJf8DPVwLPZMjVn8Gfzlt7qUXSscdDtl4UN6_hjso6hVYRhaW-475kzoR4J7nSHZUnbwjkdY36uMJNXbOFfrRwvyvVADTriAG1SO51IRJQqlyVU-zTnmfhJGiAfjrnG8ksLZQp0rk6_62GYhMcoo__K9iIe5NoJ5rGqn3I3ex6_RSrASnckpEeN-LED-1pHS14Ks5F2KIeanXISKV71DaShwurUZSUA6-mkBIbopklKwepypBWHy483Vg";

        BasicSessionCredentials cred = new BasicSessionCredentials(tmpSecretId, tmpSecretKey, sessionToken);
        // ClientConfig 中包含了后续请求 COS 的客户端设置：
        ClientConfig clientConfig = new ClientConfig();
        // 设置 bucket 的地域
        // COS_REGION 请参见 https://cloud.tencent.com/document/product/436/6224
        clientConfig.setRegion(new Region("ap-beijing"));
        // 设置请求协议, http 或者 https
        // 5.6.53 及更低的版本，建议设置使用 https 协议
        // 5.6.54 及更高版本，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 以下的设置，是可选的：
        // 设置 socket 读取超时，默认 30s
        clientConfig.setSocketTimeout(30*1000);
        // 设置建立连接超时，默认 30s
        clientConfig.setConnectionTimeout(30*1000);
        // 如果需要的话，设置 http 代理，ip 以及 port
//        clientConfig.setHttpProxyIp("httpProxyIp");
//        clientConfig.setHttpProxyPort(80);
        // 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }
    public String putObject(String FileName) throws InterruptedException {
        Instant timeStamp = Instant.now();
        String localFilePath = "uploaded/"+FileName+"_"+timeStamp.toString()+".pptx";
        COSClient cosClient = createCOSClient();
        File localFile = new File(localFilePath);
    // 指定文件将要存放的存储桶
        String bucketName = "ppt-helper-1300870583";
    // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, FileName, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        System.out.println(putObjectResult);
        return putObjectResult.toString();
    }

    public Put2Cos() {
        cosClient = createCOSClient();
    }

    public static void main(String[] args) {
        Put2Cos cos = new Put2Cos();
        try {
            System.out.println(cos.putObject("my12-3.pptx"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
