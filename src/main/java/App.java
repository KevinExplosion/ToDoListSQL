import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import spark.Request.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("allCategories", Category.all());
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category myCategory = new Category(request.queryParams("newCategory"));
      myCategory.save();
      model.put("allCategories", Category.all());
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("category", Category.find(Integer.parseInt(request.params(":id"))));
      model.put("template", "templates/categoryTasks.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int categoryID = Integer.parseInt(request.params(":id"));
      String taskName = request.queryParams("newTask");
      String newDueDate = request.queryParams("taskDueDate");
      Task newTask = new Task(taskName, categoryID, newDueDate);
      newTask.save();
      //model.put("allTasks", Category.find(categoryID).getTasks());
      model.put("category", Category.find(categoryID));
      model.put("template", "templates/categoryTasks.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories/delete/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int categoryID = Integer.parseInt(request.params(":id"));

      String[] deletedTaskIDs = request.queryParamsValues("deleteTask");
      for (String taskID : deletedTaskIDs) {
        Task foundTask = Task.find(Integer.parseInt(taskID));
        foundTask.delete();
      }
      model.put("category", Category.find(categoryID));
      model.put("template", "templates/categoryTasks.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/priority", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("priorityTasks", Task.getPriorityTasks());
      model.put("template", "templates/priority.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
