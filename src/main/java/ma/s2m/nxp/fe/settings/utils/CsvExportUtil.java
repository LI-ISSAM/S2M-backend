package ma.s2m.nxp.fe.settings.utils;

import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Génère un CSV à partir d'un en-tête et de lignes brutes.
 * Réutilisable pour n'importe quelle entité : chaque contrôleur ne fait
 * que construire headers/rows à partir de ses DTO.
 */
public class CsvExportUtil {

    private CsvExportUtil() {
    }

    public static byte[] toCsv(List<String> headers, List<String[]> rows) {
        try (StringWriter sw = new StringWriter();
             CSVWriter writer = new CSVWriter(sw)) {

            writer.writeNext(headers.toArray(new String[0]));
            for (String[] row : rows) {
                writer.writeNext(row);
            }
            writer.flush();

            // BOM UTF-8 pour qu'Excel affiche correctement les accents (é, à, ç...)
            byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            byte[] content = sw.toString().getBytes(StandardCharsets.UTF_8);
            byte[] result = new byte[bom.length + content.length];
            System.arraycopy(bom, 0, result, 0, bom.length);
            System.arraycopy(content, 0, result, bom.length, content.length);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du CSV", e);
        }
    }
}