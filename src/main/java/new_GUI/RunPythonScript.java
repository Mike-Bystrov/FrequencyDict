package new_GUI;

import new_database.WordRepository;

import java.io.*;
import java.sql.SQLException;

public class RunPythonScript {
    public static void run() throws SQLException, IOException{
//PrintWriter pw = new PrintWriter("C:\\Users\\User\\Documents\\python_data.txt");
//        for (int freq = 1; freq<=4500; freq++) {
//            pw.print(WordRepository.countNumOfWordsWithFrequency(freq) + " ");
//        }
//        pw.close();
        try {
            // Задайте путь к вашему Python-скрипту
            String pythonScriptPath = "C:\\Users\\User\\Documents\\plot_hist.py";

            // Создайте процесс для выполнения Python-скрипта
            Process process = Runtime.getRuntime().exec("python " + pythonScriptPath);

            // Получите вывод скрипта (если это необходимо)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Дождитесь завершения выполнения скрипта
            int exitCode = process.waitFor();

            // Выведите код завершения (0 обычно означает успешное выполнение)
            System.out.println("Код завершения: " + exitCode);
            //pw.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException, SQLException {
        run();
    }
}
