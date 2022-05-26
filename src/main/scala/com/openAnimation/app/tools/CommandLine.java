package com.openAnimation.app.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLine {

    public static String executeCommand(String cmd) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line=buf.readLine())!=null) {
            System.out.println(line);
        }
        return line;
    }
}
