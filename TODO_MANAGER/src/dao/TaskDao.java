package dao;

import model.Task;
// import model.User;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
	List<Task> taskList = new ArrayList<>();
	Connection conn = DBConnection.getConnection();

	public void addTask(Task task) throws Exception {
		String sql = "INSERT INTO tasks (taskId, taskTitle, taskText, assignedTo, isCompleted) VALUES (?,?, ?, ?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(5, task.getTaskId());
			statement.setString(1, task.getTaskTitle());
			statement.setString(2, task.getTaskText());
			statement.setString(3, task.getAssignedTo());
			statement.setBoolean(4, task.isTaskCompleted());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
		taskList.add(task);
	}

	public void updateTask(Task task) throws Exception {
		String sql = "UPDATE tasks SET taskTitle = ?, taskText = ?, assignedTo = ?, isCompleted = ? WHERE taskId = ?";

		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, task.getTaskTitle());
			statement.setString(2, task.getTaskText());
			statement.setString(3, task.getAssignedTo());
			statement.setBoolean(4, task.isTaskCompleted());
			statement.setInt(5, task.getTaskId());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
	}
	public void deleteTask(Task taskToDelete) throws Exception {
		String sql = "DELETE FROM tasks WHERE taskId = ?";

		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, taskToDelete.getTaskId());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
		taskList.removeIf(task -> task.getTaskId() == taskToDelete.getTaskId());
	}

	public List<Task> searchTasks(String searchQuery, String email) throws Exception {
		List<Task> searchResults = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE taskTitle LIKE ? and assignedTo = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "%" + searchQuery + "%");
            statement.setString(2, email);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int taskId = rs.getInt("taskId");
                String taskTitle = rs.getString("taskTitle");
                String taskText = rs.getString("taskText");
                boolean taskCompleted = rs.getBoolean("isCompleted");
                String assignedTo = rs.getString("assignedTo");
                Task task = new Task(taskId, taskTitle, taskText, assignedTo, taskCompleted);
                searchResults.add(task);
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
        return searchResults;
	}

	public List<Task> listAllTasks(String email) throws Exception {
		List<Task> allTasks = new ArrayList<>();

		String sql = "select * from tasks";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int taskId = rs.getInt("taskId");
				String taskTitle = rs.getString("taskTitle");
				String taskText = rs.getString("taskText");
				String assignedTo = rs.getString("assignedTo");
				boolean taskCompleted = rs.getBoolean("isCompleted");
				Task task = new Task(taskId, taskTitle, taskText, assignedTo, taskCompleted);
				allTasks.add(task);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
		return allTasks;
	}

	public List<Task> getAllTasks(boolean completed) throws Exception {
		List<Task> tasks = new ArrayList<>();
		String sql = "SELECT * FROM tasks WHERE completed = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setBoolean(1, completed);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Task task = new Task(rs.getInt("id"), rs.getString("title"), rs.getString("text"),
						rs.getString("assigned_to"), rs.getBoolean("completed"));
				tasks.add(task);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return tasks;
	}

	public List<Task> getTaskByStatus(String email, boolean Status) throws Exception {
		List<Task> statusList = new ArrayList<>();
	    String sql = "SELECT * FROM tasks WHERE isCompleted=?";
	    try {
	        PreparedStatement statement = conn.prepareStatement(sql);
	        statement.setBoolean(1, Status);
	        ResultSet rs = statement.executeQuery();
	        while (rs.next()) {
	            Task task = new Task();
	            task.setTaskTitle(rs.getString("taskTitle"));
	            task.setTaskText(rs.getString("taskText"));
	            task.setAssignedTo(rs.getString("assignedTo"));
	            task.setTaskCompleted(rs.getBoolean("isCompleted"));
	            task.setTaskId(rs.getInt("taskId"));
	            statusList.add(task);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return statusList;
	}

	public List<Task> getIncompleTaskByStatus(String email, boolean status) {
	    List<Task> statusList = new ArrayList<>();
	    String sql = "SELECT * FROM tasks WHERE isCompleted=?";
	    try {
	        PreparedStatement statement = conn.prepareStatement(sql);
	        statement.setBoolean(1, status);
	        ResultSet rs = statement.executeQuery();
	        while (rs.next()) {
	            Task task = new Task();
	            task.setTaskTitle(rs.getString("taskTitle"));
	            task.setTaskText(rs.getString("taskText"));
	            task.setAssignedTo(rs.getString("assignedTo"));
	            task.setTaskCompleted(rs.getBoolean("isCompleted"));
	            task.setTaskId(rs.getInt("taskId"));
	            statusList.add(task);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return statusList;
	}


	public Task getTaskById(int taskId) throws Exception {
		String sql = "SELECT * FROM tasks WHERE taskId=?";
		Task task = null;
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, taskId);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					task = new Task();
					task.setTaskId(rs.getInt("taskId"));
					task.setTaskTitle(rs.getString("taskTitle"));
					task.setTaskText(rs.getString("taskText"));
					task.setAssignedTo(rs.getString("assignedTo"));
					task.setTaskCompleted(rs.getBoolean("isCompleted"));
				} else {
					throw new Exception("No task with ID " + taskId + " found.");
				}
			}
		} catch (SQLException e) {
			throw new Exception("Error retrieving task with ID " + taskId + ": " + e.getMessage());
		}
		return task;
	}

}
