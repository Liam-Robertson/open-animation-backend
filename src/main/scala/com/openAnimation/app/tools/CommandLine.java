package com.openAnimation.app.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class CommandLine {

    public static void executeCommand(String cmd) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmd);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
    }

    public static void executeFfmpeg(String[] cmd) throws IOException, InterruptedException {
        Process processDuration = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        StringBuilder strBuild = new StringBuilder();
        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()));) {
            String line;
            while ((line = processOutputReader.readLine()) != null) {
                strBuild.append(line + System.lineSeparator());
            }
            processDuration.waitFor();
        }
        String outputJson = strBuild.toString().trim();
        System.out.println(outputJson);
    }

    public static String getFfmpegOutput(String[] cmd) throws IOException, InterruptedException {
        Process processDuration = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        StringBuilder strBuild = new StringBuilder();
        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()));) {
            String line;
            while ((line = processOutputReader.readLine()) != null) {
                strBuild.append(line + System.lineSeparator());
            }
            processDuration.waitFor();
        }
        String outputJson = strBuild.toString().trim();
        return outputJson;
    }
}
