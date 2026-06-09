package com.hessati.hessati.services;

import com.hessati.hessati.dto.ShareProductDTO;
import com.hessati.hessati.dto.StronixInfoDTO;
import com.hessati.hessati.entities.StronixInfo;
import com.hessati.hessati.repositories.StronixInfoRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StronixInfoService {

    @Autowired
    private StronixInfoRepository stronixInfoRepository;
    @Autowired
    private JavaMailSender mailSender;

    public StronixInfo getStronixInfo() {
        List<StronixInfo> list = stronixInfoRepository.findAll();
        if (list.isEmpty()) {
            return new StronixInfo();
        }
        return list.get(0);
    }

    public StronixInfo updateStronixInfo(StronixInfoDTO stronixInfoDTO) {
        List<StronixInfo> list = stronixInfoRepository.findAll();
        StronixInfo stronixInfo;
        if (list.isEmpty()) {
            stronixInfo = new StronixInfo();
        } else {
            stronixInfo = list.get(0);
        }
        stronixInfo.setTel(stronixInfoDTO.getTel());
        stronixInfo.setEmailComercial(stronixInfoDTO.getEmailComercial());
        stronixInfo.setLocation(stronixInfoDTO.getLocation());
        return stronixInfoRepository.save(stronixInfo);
    }

    public Boolean shareProduct(ShareProductDTO shareProductInfo) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("stronix@stronix-rx.com");
            helper.setTo(shareProductInfo.getEmail());
            helper.setSubject("Stronix: Check out this product!");
            String htmlContent = "<!DOCTYPE html>"
                    + "<html lang='en'>"
                    + "<head>"
                    + "  <meta charset='UTF-8'>"
                    + "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "  <style>"
                    + "    .button {"
                    + "      background-color: #007bff;"
                    + "      color: white;"
                    + "      padding: 12px 20px;"
                    + "      text-align: center;"
                    + "      text-decoration: none;"
                    + "      display: inline-block;"
                    + "      font-size: 16px;"
                    + "      border-radius: 5px;"
                    + "      margin-top: 10px;"
                    + "    }"
                    + "    .container {"
                    + "      font-family: Arial, sans-serif;"
                    + "      padding: 20px;"
                    + "      line-height: 1.6;"
                    + "      background-color: #f9f9f9;"
                    + "    }"
                    + "    .content {"
                    + "      background-color: #ffffff;"
                    + "      padding: 20px;"
                    + "      border-radius: 8px;"
                    + "      border: 1px solid #e0e0e0;"
                    + "    }"
                    + "  </style>"
                    + "</head>"
                    + "<body>"
                    + "  <div class='container'>"
                    + "    <div class='content'>"
                    + "      <h3>Hello,</h3>"
                    + "      <p><strong>" + shareProductInfo.getFullname() + "</strong> has shared this product with you:</p>"
                    + "      <p style='text-align:center;'>"
                    + "<a href='" + shareProductInfo.getProductLink() + "' "
                    + "style='background-color:#007bff;color:#ffffff;padding:12px 20px;"
                    + "text-decoration:none;display:inline-block;font-size:16px;border-radius:5px;' "
                    + ">View Product</a>"
                    + "      </p>"
                    + "      <p>Best regards,<br/>Welcome to Stronix</p>"
                    + "    </div>"
                    + "  </div>"
                    + "</body>"
                    + "</html>";
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
