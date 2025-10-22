package com.example.sudharshanlogistics.util;

import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryReceiptResponseDto;

public class PdfGenerator {

    public static byte[] generateLorryInvoicePdf(LorryReceiptResponseDto responseDto) {
        // Placeholder implementation
        // In production, use a PDF library like iText or Apache PDFBox to generate PDF bytes
        String pdfContent = "Lorry Invoice PDF for WayBill No: " + responseDto.getWayBillNo();
        return pdfContent.getBytes();
    }
}