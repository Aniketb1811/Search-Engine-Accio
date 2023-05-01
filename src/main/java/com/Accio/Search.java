package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //Getting keyword from user in frontend
        String keyword = request.getParameter("keyword");

        // Setting up connection to DB
        Connection connection = DatabaseConnection.getConnection();
        try {
            //Storing keyword from frontend to History table
            PreparedStatement preparedStatement = connection.prepareStatement("insert into history values(?, ?);");
            preparedStatement.setString(1, keyword);
            preparedStatement.setString(2, "http://localhost:8080/Search?keyword="+ keyword);
            preparedStatement.executeUpdate();

            //Getting results after running the ranking query
            ResultSet resultSet = connection.createStatement().executeQuery("select pageTitle, pageLink, (length(lower(pageText)) - length(replace(lower(pageText), '"+ keyword.toLowerCase() +"', ''))) / length('"+ keyword.toLowerCase() +"') as countoccurence from pages order by countoccurence desc limit 30;");
            ArrayList<SearchResult> results = new ArrayList<>();

            //Transferring values from resultSet to result arrayList
            while (resultSet.next()) {
                SearchResult searchResult = new SearchResult();
                searchResult.setTitle(resultSet.getString("pageTitle"));
                searchResult.setLink(resultSet.getString("pageLink"));
                results.add(searchResult);
            }
            // Displaying results arraylist
//            for(SearchResult result:results){
//                System.out.println(result.getTitle() +"\n"+result.getLink()+"\n");
//            }

            request.setAttribute("results", results);
            request.getRequestDispatcher("Search.jsp").forward(request, response);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
        }
        catch (ServletException | SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
