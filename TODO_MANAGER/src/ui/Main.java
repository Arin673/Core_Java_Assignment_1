package ui;

import java.util.List;
import java.util.Scanner;

import model.User;
import model.Task;
import dao.TaskDao;
import dao.UserDao;

public class Main {

    private static final Scanner ob = new Scanner(System.in);
    private static TaskDao taskDao = new TaskDao();
    private static UserDao userDao = new UserDao();

    public static void main(String[] args) {
        User currentUser = authenticateUser();

        boolean exit = false;

        while(!exit){
            System.out.println("Please choose an operation:");
            System.out.println("1. Add task");
            System.out.println("2. Update task");
            System.out.println("3. Delete task");
            System.out.println("4. Search task");
            System.out.println("5. List all tasks");
            System.out.println("6. View completed tasks");
            System.out.println("7. View incomplete tasks");
            System.out.println("0. Exit!");

            int operation = ob.nextInt();
            switch(operation)
            {
                case 1:
				try {
					addTask(currentUser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                    break;
                case 2:
                    updateTask(currentUser);
                    break;
                case 3:
                    deleteTask(currentUser);
                    break;
                case 4:
                    searchTasks(currentUser);
                    break;
                case 5:
                    viewAllTasks(currentUser);
                    break;
                case 6:
                    viewCompletedTasks(currentUser);
                    break;
                case 7:
                    viewIncompleteTasks(currentUser);
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static User authenticateUser() {
        System.out.println("Enter your email address:");
        String email = ob.nextLine();

        System.out.println("Enter your password:");
        String password = ob.nextLine();

        User currentUser=null;
		try {
			currentUser = userDao.getUserByEmailAndPassword(email, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (currentUser == null) {
            System.out.println("Invalid Credentials!");
            return authenticateUser();
        }

        System.out.println("Welcome, " + currentUser.getUserName() + "!");
        return currentUser;
    }

    private static void addTask(User currentUser) throws Exception{
    	System.out.println("Enter Task Id:");
        int taskId = ob.nextInt();
        ob.nextLine();
        System.out.println("Enter Task Title:");
        String title = ob.nextLine();
        System.out.println("Enter Task Description:");
        String description = ob.nextLine();
        System.out.println("Enter Task Status");
        boolean status = ob.nextBoolean();
        
        Task task = new Task();
        task.setTaskId(taskId);
        task.setTaskTitle(title);
        task.setTaskText(description);
        task.setAssignedTo(currentUser.getEmail());
        task.setTaskCompleted(status);
        taskDao.addTask(task);

        System.out.println("Task Added!");
    }

    private static void updateTask(User currentUser) {
            System.out.println("Enter the Task ID to update the Task:");
            int taskId = ob.nextInt();

            ob.nextLine();

            Task taskToUpdate=null;
			try {
				taskToUpdate = taskDao.getTaskById(taskId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            if (taskToUpdate == null)
            {
                System.out.println("Invalid Task ID.");
                return;
            }
 
            if (!taskToUpdate.getAssignedTo().equals(currentUser.getEmail())) {

                System.out.println("You cannot update this task.");
                return;
            }

            System.out.println("Enter the updated task title (To skip press Enter):");
            String newTaskTitle = ob.nextLine().trim();

            System.out.println("Enter the updated task description (To skip press Enter):");
            String newTaskText = ob.nextLine().trim();

            if (!newTaskTitle.isEmpty()) {
                taskToUpdate.setTaskTitle(newTaskTitle);
            }

            if (!newTaskText.isEmpty()) {
                taskToUpdate.setTaskText(newTaskText);
            }

            try {
				taskDao.updateTask(taskToUpdate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            System.out.println("Task Updated!");
    }

    private static void deleteTask(User currentUser) {
        System.out.println("Enter the Task ID to delete the Task:");
        int taskId = ob.nextInt();
        ob.nextLine();

        Task taskToDelete=null;
		try {
			taskToDelete = taskDao.getTaskById(taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (taskToDelete == null) {
            System.out.println("Invalid Task ID.");
            return;
        }

        if (!taskToDelete.getAssignedTo().equals(currentUser.getEmail()))
        {
            System.out.println("You cannot delete this task.");
            return;
        }

        try {
			taskDao.deleteTask(taskToDelete);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println("Task deleted!");

    }

    private static void searchTasks(User currentUser) {
        System.out.println("Enter keyword to search for task titles and descriptions:");
        String keyword = ob.nextLine().trim();
        ob.nextLine();
        List<Task> searchResults=null;
		try {
			searchResults = taskDao.searchTasks(keyword, currentUser.getEmail());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (searchResults.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Matching tasks:");

            for (Task task : searchResults)
            {
                System.out.println(task);
            }
        }
    }

    private static void viewAllTasks(User currentUser) {
        List<Task> tasks=null;
		try {
			tasks = taskDao.listAllTasks(currentUser.getEmail());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("All tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private static void viewCompletedTasks(User currentUser) {
        List<Task> tasks=null;
		try {
			tasks = taskDao.getTaskByStatus(currentUser.getEmail(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (tasks.isEmpty()) {
            System.out.println("No completed tasks found.");
        }
        else
        {
            System.out.println("Completed tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private static void viewIncompleteTasks(User currentUser) {
        List<Task> tasks=null;
		try {
			tasks = taskDao.getIncompleTaskByStatus(currentUser.getEmail(), false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (tasks.isEmpty()) {
            System.out.println("No incomplete tasks found.");
        } else {
            System.out.println("Incomplete tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }
}
