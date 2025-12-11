import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class HighScoresDisplay {

    private static boolean isDisplayed = false; 

    public static void addScore(String fileName, String playerName, int score) {
        try {
            File file = new File(fileName);
            List<String> scoresList = new ArrayList<>();

            // لو الملف موجود، اقراه كله الأول
            if (file.exists()) {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    scoresList.add(sc.nextLine());
                }
                sc.close();
            }

            scoresList.add(playerName + "-" + score);

            scoresList.sort((a, b) -> {
                int scoreA = Integer.parseInt(a.split("-")[1]);
                int scoreB = Integer.parseInt(b.split("-")[1]);
                return scoreB - scoreA; 
            });

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String s : scoresList) {
                bw.write(s);
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void showHighScores(String filePath, boolean isNewGame) {
        if (isNewGame) {
            isDisplayed = false;
        }

        if (isDisplayed) {
            return;
        }

        isDisplayed = true; 

        List<String> scores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                scores.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("High Scores");
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (String s : scores) {
            JLabel label = new JLabel(s);
            label.setFont(new Font("Arial", Font.PLAIN, 18));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        }

        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(closeButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}
