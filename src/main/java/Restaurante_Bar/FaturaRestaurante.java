package Restaurante_Bar;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class FaturaRestaurante {

    
    private static final String PASTA_FATURAS = "faturas";

    public static void gerar(Cliente cliente, List<Pedido> pedidos) {

        
        File pasta = new File(PASTA_FATURAS);
            if (!pasta.exists() || !pasta.isDirectory()) {
                throw new RuntimeException(
                    "A pasta de faturas não existe: " + pasta.getAbsolutePath() +
                    "\nCria manualmente a pasta 'faturas' na raiz do projeto."
                );
            }

        String nomeArquivo = "Fatura_" + cliente.getNome().replace(" ", "_") + ".pdf";
        String caminhoFatura = PASTA_FATURAS + File.separator + nomeArquivo;

        DecimalFormat df = new DecimalFormat("#,##0.00");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(caminhoFatura));

            document.open();

            // Fontes
            Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 12);
            Font negrito = new Font(Font.HELVETICA, 12, Font.BOLD);

            // Cabeçalho
            Paragraph pTitulo = new Paragraph("RESTAURANTE/BAR CENTRAL", titulo);
            pTitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(pTitulo);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Cliente: " + cliente.getNome(), normal));
            document.add(new Paragraph("--------------------------------------------------", normal));
            document.add(new Paragraph(" "));

            // Tabela
            PdfPTable tabela = new PdfPTable(4);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{45f, 15f, 10f, 20f});

            tabela.addCell(new Phrase("Produto", negrito));
            tabela.addCell(new Phrase("Preço", negrito));
            tabela.addCell(new Phrase("Qtd", negrito));
            tabela.addCell(new Phrase("Total", negrito));

            double totalGeral = 0;

            for (Pedido p : pedidos) {
                double valor = p.getTotal();
                totalGeral += valor;

                tabela.addCell(new Phrase(p.getProduto().getNome(), normal));
                tabela.addCell(new Phrase(df.format(p.getProduto().getPreco()) + " ECV", normal));
                tabela.addCell(new Phrase(String.valueOf(p.getQuantidade()), normal));
                tabela.addCell(new Phrase(df.format(valor) + " ECV", normal));
            }

            document.add(tabela);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("--------------------------------------------------", normal));
            document.add(new Paragraph("TOTAL A PAGAR: " + df.format(totalGeral) + " ECV", negrito));

            document.close();

            System.out.println("Fatura PDF gerada em: " + caminhoFatura);

        } catch (Exception e) {
            System.out.println("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
