import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PocessTest {
    static class OSExecuteException extends RuntimeException {
        public OSExecuteException(String why) {
            super(why);
        }
    }

    static class OSExecute {
        public static void command(String command) {
            boolean err = false;
            try {
                Process process = new ProcessBuilder(command.split(" ")).start();
                BufferedReader results = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s;
                while ((s = results.readLine()) != null) {
                    System.out.println(s);
                }

                BufferedReader errors = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((s = errors.readLine()) != null) {
                    System.out.println(s);
                    err = true;
                }

                System.out.println();

                Process process1 = Runtime.getRuntime().exec(command);
                process1.waitFor();
                InputStream inputStream = process1.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                while ((s = reader.readLine()) != null) {
                    System.out.println(s);
                }

            } catch (Exception e) {
                if (!command.startsWith("CMD /C")) {
                    command("CMD /C " + command);
                } else {
                    throw new RuntimeException(e);
                }
            }
            if (err) {
                throw new OSExecuteException("Errors executing " + command);
            }
        }

        public static void main(String[] args) {
            command("jps");
        }
    }
}
