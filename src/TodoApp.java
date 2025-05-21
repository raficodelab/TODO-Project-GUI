import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

interface TaskObserver {
    void update(Task task);
}

class Task {
    private String title;
    private boolean completed;
    private List<TaskObserver> observers = new ArrayList<>();

    public Task(String title) {
        this.title = title;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        if (!completed) {
            this.completed = true;
            notifyObservers();
        }
    }

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (TaskObserver obs : observers) {
            obs.update(this);
        }
    }

    @Override
    public String toString() {
        return title + (completed ? " (Completed)" : "");
    }
}

public class TodoApp extends JFrame implements TaskObserver {

    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JButton markCompletedButton;
    private JLabel statusLabel;
    private List<Task> tasks = new ArrayList<>();

    public TodoApp() {
        setTitle("TODO Application - Mark Item as Completed");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        markCompletedButton = new JButton("Mark Completed");
        statusLabel = new JLabel("Select a task to mark as completed.");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(markCompletedButton, BorderLayout.WEST);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        add(panel);

        addTask(new Task("Finish homework"));
        addTask(new Task("Buy groceries"));
        addTask(new Task("Call Mom"));

        markCompletedButton.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                if (!selectedTask.isCompleted()) {
                    selectedTask.markCompleted();
                } else {
                    statusLabel.setText("Task already completed!");
                }
            } else {
                statusLabel.setText("Please select a task first.");
            }
        });
    }

    private void addTask(Task task) {
        tasks.add(task);
        task.addObserver(this);
        taskListModel.addElement(task);
    }

    public void update(Task task) {
        taskList.repaint();
        statusLabel.setText("Task '" + task.getTitle() + "' marked as completed.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoApp app = new TodoApp();
            app.setVisible(true);
        });
    }
}
