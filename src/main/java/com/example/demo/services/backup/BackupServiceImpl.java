package com.example.demo.services.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BackupServiceImpl implements BackupService{
    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${backup.mysql.database}")
    private String dbName;

    @Value("${backup.folder.path.win}")
    private String backupFilePathWindows; //dejo el de windows pq todos tenemos windows :), si hosteamos habria que ver

    //TODO: Revisar si agregamos un if o algo para tener en cuenta MAC y Linux

    public List<String> listarBackups() {
        File carpeta = new File(backupFilePathWindows);
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            return new ArrayList<>();
        }
        File[] archivos = carpeta.listFiles();
        List<String> lista = new ArrayList<String>();
        if (archivos != null) {
            for (File archivo : archivos) {
                lista.add(archivo.getName());
                System.out.println(archivo.getName());
            }
        }
        return lista;
    }

    public String generarBackup() {
        try {
            String fechaHora = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                                  .format(Calendar.getInstance().getTime());
            String outputFile = backupFilePathWindows + File.separator + "backup" + fechaHora + ".sql";

            //TODO: Revisar si agregamos un if o algo para tener en cuenta MAC y Linux

            File carpeta = new File(backupFilePathWindows);
            if (!carpeta.exists()) carpeta.mkdirs();
            
            List<String> comando = List.of(
                "mysqldump",
                "-u", dbUsername,
                "-p" + dbPassword,
                dbName,
                "-r", outputFile
            );
            ejecutarProceso(comando, null);

            return "Backup generado correctamente: " + outputFile;
        } catch (Exception e) {
            return "Error al generar backup: " + e.getMessage();
        }
    }

    public String restaurarBackup(String nombreBackup) {
        try {
            String inputFile = backupFilePathWindows + File.separator + nombreBackup;

            File archivoExistente = new File(inputFile);

            if (!archivoExistente.exists() || !archivoExistente.isFile()) {
                throw new FileNotFoundException("El backup '" + nombreBackup + "' no existe en la carpeta de backups.");
            }

            List<String> comando = List.of(
                "mysql",
                "-u", dbUsername,
                "-p" + dbPassword,
                dbName
            );

            ejecutarProceso(comando, archivoExistente);

            return "Restauración completada correctamente";
        } catch (Exception e) {
            return "Error al restaurar backup: " + e.getMessage();
        }
    }

    private void ejecutarProceso(List<String> comando, File inputFile){
        ProcessBuilder processBuilder = new ProcessBuilder(comando);
        processBuilder.redirectErrorStream(true);

        if (inputFile != null) {
            processBuilder.redirectInput(inputFile);
        }

        try {
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new Exception("El proceso falló con código " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
