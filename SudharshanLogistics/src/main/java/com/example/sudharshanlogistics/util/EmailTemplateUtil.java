package com.example.sudharshanlogistics.util;


import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryReceiptResponseDto;

public class EmailTemplateUtil {

    public static String generateInvoiceEmailBody(LorryReceiptResponseDto responseDto) {
        return """
                <html>
                     <body style="font-family: Arial, sans-serif; color: #000;">
                         <p>Dear <b>%s</b>,</p>
                         
                         <p>Greetings from <b>SUDARSHAN ROADLINES!</b></p>
                         
                         <p>Please find the attached document related to
                         <b>the Lorry Receipt dated %s</b> for your kind reference.</p>
                         
                         <p><b>Details:</b></p>
                          <ul>
                                <li><b>Way Bill No:</b> %s</li>
                                <li><b>Date:</b> %s</li>
                                <li><b>Vehicle No:</b> %s</li>
                                <li><b>Consignor:</b> %s</li>
                                <li><b>Consignee:</b> %s</li>
                           </ul>
                         
                          <p>Kindly review the document and feel free to contact us
                          in case of any queries or clarifications.</p>
                          
                          <p>Looking forward to your acknowledgment.</p>
                          
                           <p>Best regards,</p>
                           <p><b>Head Office</b><br>
                           SUDARSHAN ROADLINES<br>
                           <a href="mailto:sudarshanlogisticsshiroli@yahoo.com">sudarshanlogisticsshiroli@yahoo.com</a></p>
                         
                           <hr>
                             <p style="font-size: 12px; color: gray;">
                                This is an automated email sent from the logistics system.
                                Please do not reply directly to this message.
                             </p>
             
                </body>
                </html>
                """.formatted(
                responseDto.getItems() != null && !responseDto.getItems().isEmpty() && responseDto.getItems().get(0).getRoute() != null
                        ? responseDto.getItems().get(0).getRoute().getConsigneeId().getPartyName() : "N/A",
                responseDto.getDate(),
                responseDto.getWayBillNo(),
                responseDto.getDate(),
                responseDto.getItems() != null && !responseDto.getItems().isEmpty() && responseDto.getItems().get(0).getRoute() != null
                        ? responseDto.getItems().get(0).getRoute().getVehicle().getVehicleNumber() : "N/A",
                responseDto.getItems() != null && !responseDto.getItems().isEmpty() && responseDto.getItems().get(0).getRoute() != null
                        ? responseDto.getItems().get(0).getRoute().getConsignerId().getPartyName(): "N/A",
                responseDto.getItems() != null && !responseDto.getItems().isEmpty() && responseDto.getItems().get(0).getRoute() != null
                        ? responseDto.getItems().get(0).getRoute().getConsigneeId().getPartyName() : "N/A"
        );
    }
}
