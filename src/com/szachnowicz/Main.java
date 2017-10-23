package com.szachnowicz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {

        final Main main = new Main();
        final CCLoader ccLoader = new CCLoader(Main.class.getClassLoader());




        final List<String> allClassName = getAllThisProgramClasses();

        JFrame frame = bulidFrame();
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String str : allClassName) {
            model.addElement(str);
        }

        JList<String> list = new JList<>(model);
        frame.add(list);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String name = allClassName.get(list.getSelectedIndex());
                try {
                    Class aClass = ccLoader.loadClass(name);
                    showClassDetalis(aClass);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        frame.add(okButton);
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 100));
        frame.add(textField);


        JButton pathButton = new JButton("Szukaj");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

        frame.add(pathButton);





    }

    private static void showClassDetalis(Class className) {


        JFrame frame = new JFrame(className.getName());
        frame.setSize(400, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container container = frame.getContentPane();
        container.setLayout(new FlowLayout());

        JTextArea methodTextArea = new JTextArea();
        methodTextArea.setPreferredSize(new Dimension(370, 400));
        methodTextArea.setEditable(false);

        final JScrollPane jScrollPane = new JScrollPane(methodTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        JTextArea fieldsTextArea = new JTextArea();
        fieldsTextArea.setPreferredSize(new Dimension(370, 200));
        fieldsTextArea.setEditable(false);

        String methods = new Main().printMethod(className);
        String fileds = new Main().printFields(className);

        methodTextArea.setText(methods);
        fieldsTextArea.setText(fileds);

        container.add(fieldsTextArea);
        container.add(methodTextArea);

        frame.setVisible(true);


    }

    private static List<String> getAllThisProgramClasses() {
        final String folder1 = "folder";
        final File folder = new File(folder1);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String pathWithThisProgram = folder.getAbsolutePath();

        pathWithThisProgram = pathWithThisProgram.replace(folder1, "");

        return getAllClassesFromThisPath(pathWithThisProgram);
    }

    private static JFrame bulidFrame() {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new FlowLayout());
        frame.setVisible(true);
        return frame;
    }

    private static List<String> getAllClassesFromThisPath(String pathWithThisProgram) {
        List<String> pathWithClasses = new LinkedList<>();
        final List<String> strings = fileRecursive(pathWithThisProgram);
        for (String string : strings) {
            if (string.endsWith(".java")) {
                string = string.replace(pathWithThisProgram, "");
                pathWithClasses.add(getOnlyPackedNameFromPath(string));
            }
        }

        return pathWithClasses;

    }

    private static String getOnlyPackedNameFromPath(String pathWithClass) {
        pathWithClass = pathWithClass.replace("\\src\\", "");
        pathWithClass = pathWithClass.replace(".java", "");
        pathWithClass = pathWithClass.replace("\\", ".");
        return pathWithClass;
    }

    public static List<String> fileRecursive(String path) {
        List<String> pathList = new LinkedList<>();

        String[] list = new File(path).list();
        for (String s : list) {
            final String newFolderPAth = path + "\\" + s;
            pathList.add(newFolderPAth);
            if (new File(newFolderPAth).isDirectory())
                pathList.addAll(fileRecursive(newFolderPAth));
        }


        return pathList;
    }


    private void printClass(Class aClass) {
        for (int i = 0; i < 2; i++) {
            System.out.println();
        }
        printFields(aClass);
        printMethod(aClass);

    }

    private String printFields(Class aClass) {
        StringBuilder sb = new StringBuilder();

        sb.append("_________________Fields___________________\n");
        for (Field field : aClass.getFields()) {

            sb.append(printType(field.getGenericType().getTypeName()) + " " + field.getName());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String printMethod(Class aClass) {
        StringBuilder sb = new StringBuilder();
        sb.append("_________________Constructor___________________ \n");
        for (Constructor constructor : aClass.getConstructors()) {

            sb.append(printType(constructor.getName()));

            sb.append("(");


            final Class[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {

                sb.append(printType(parameterTypes[i].getName()));
                if (i >= 0 && i < parameterTypes.length - 1)
                    sb.append(",");
            }
            sb.append(")\n");
        }

        sb.append("\n");

        sb.append("_________________Methods___________________ \n");
        for (Method method : aClass.getMethods()) {
            String returnType = method.getReturnType().toString();
            returnType = printType(returnType);
            sb.append(returnType + " " + method.getName() + "");
            sb.append("(");

            final Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {

                sb.append(printType(parameterTypes[i].getName()));
                if (i >= 0 && i < parameterTypes.length - 1)
                    sb.append(",");
            }
            sb.append(")\n");
        }


        System.out.println(sb);
        return sb.toString();


    }

    private String printType(String string) {

        if (string.contains(".") && !string.contains("<")) {
            final String[] split = string.split("\\.");
            if (split.length > 0) {
                return (split[split.length - 1]);
            }
        }
        return string;
    }


}
// write your code here
