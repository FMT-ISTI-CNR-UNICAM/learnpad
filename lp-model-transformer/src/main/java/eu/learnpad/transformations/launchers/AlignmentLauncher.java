package eu.learnpad.transformations.launchers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import eu.learnpad.transformations.preprocessing.Alignment;

/**
 * Class to do the alignment phase. Starting from an XML file it create a valid XMI model file.
 * 
 * @author Basciani Francesco
 */
public class AlignmentLauncher
{

    private final String ADOXX_TYPE = "ADOXX";

    private final String MAGIC_DRAW_TYPE = "MD";

    public boolean align(InputStream model, String type, OutputStream out) throws Exception
    {
        Alignment al = new Alignment();
        boolean result = false;

        switch (type) {
            case ADOXX_TYPE:
                result = al.sanitizerForADOXX(model, out);
                break;
            case MAGIC_DRAW_TYPE:
                result = al.sanitizerForMagicDraw(model, out);
                break;
            default:
                System.out.println("Type not allowed!");
                break;
        }

        return result;
    }

    public static void main(String[] args) throws Exception
    {
        String model_in = "resources/model/ado4f16a6bb-9318-4908-84a7-c2d135253dc9.xml";
        String file_out = "/tmp/testAlignmentOutputStream.xmi";
        String type = "ADOXX";
        // String type = "MD";

        AlignmentLauncher align = new AlignmentLauncher();
        // align.execute(model_in);

        // create a new output stream
        OutputStream out = new FileOutputStream(file_out);

        FileInputStream fis = new FileInputStream(model_in);
        align.align(fis, type, out);
    }
}
