import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class GUI {
    public static void main(String[] args) {
        //create gui
        String filepath;
        JFrame frame = new JFrame("Microsoft Azure REST API: Local Image Analysis");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(600, 200);
        frame.setVisible(true);

        JButton button = new JButton("Upload Files");
        button.setSize(200, 40);
        frame.add(button, BorderLayout.NORTH);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showOpenDialog(frame);
                String[] folder = new String[1];
                folder[0] = jfc.getSelectedFile().getPath();
                Local run = new Local();
                run.main(folder);
                JTextArea text = new JTextArea("Results:\n" + run.getResults());
                frame.add(text, BorderLayout.CENTER);
            }
        });

    }

}
