package com.greenfoxacademy.TribesBackend.services;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.configs.CachedBodyHttpServletRequest;
import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import lombok.Getter;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.apache.coyote.Response;
import org.flywaydb.core.internal.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.*;

@Getter
@Service
public class UtilityService {

    @Autowired
    private UserRepository userRepository;

    public String generateJWT(String ip, Long id, String username) {
        Map<String, Object> headerMap = Map.of(IP_CLAIM, ip);
        return JWT.create()
                .withHeader(headerMap)
                .withClaim(ID_CLAIM, String.valueOf(id))
                .withClaim(USERNAME_CLAIM, username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }

    public String readFile(String path) {
        path = "src/main/resources/" + path;
        String data = "";
        try {
            File myFile = new File(path);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Email content was not found");
        }
        return data;
    }

    public Long getIdFromToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");
        return Long.parseLong(JWT.decode(token).getClaim(ID_CLAIM).asString());
    }

    public ResponseEntity handleResponseWithException(FrontendException e) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("status", "error");
        modelMap.addAttribute("error", e.getMessage());
        return ResponseEntity.status(e.getSc()).body(modelMap);
    }

    public void logRequestResponse(CachedBodyHttpServletRequest request, ContentCachingResponseWrapper response, Date requestDate, Date responseDate) {
        try {
            File myFile = new File("logs.txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter("logs.txt", true);
            List<String> contents = buildLog(request, response, requestDate, responseDate);
            for (String content : contents) {
                fileWriter.write(content);
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> buildLog(CachedBodyHttpServletRequest request, ContentCachingResponseWrapper response, Date requestDate, Date responseDate) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        List<String> contents = new ArrayList<String>();
        contents.add("\nrequest date: " + simpleDateFormat.format(requestDate));
        contents.add("\nrequest http method: " + request.getMethod());
        contents.add("\nrequest headers:\n");
        contents.addAll(getRequestHeader(request));
        contents.add("request body: \n");
        contents.add(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
        contents.add("\nresponse date: " + simpleDateFormat.format(responseDate) + "\n");
        contents.add("status code: " + response.getStatus() + "\n");
        contents.add("response headers:\n");
        contents.addAll(getResponseHeaders(response));
        contents.add("response body: \n");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getContentInputStream().readAllBytes());
        contents.add(new BufferedReader(new InputStreamReader(byteArrayInputStream)).lines().collect(Collectors.joining(System.lineSeparator())));
        contents.add("\n\n");
        contents.add("-".repeat(255) + "\n");
        contents.add("-".repeat(255) + "\n");
        return contents;
    }

    public List<String> getRequestHeader(CachedBodyHttpServletRequest request) {
        List<String> contents = new ArrayList<>();
        var headerNames = Collections.list(request.getHeaderNames());
        for (String header : headerNames) {
            contents.add("  " + header + ": " + request.getHeader(header) + "\n");
        }
        return contents;
    }

    public List<String> getResponseHeaders(ContentCachingResponseWrapper response) {
        List<String> contents = new ArrayList<>();
        var headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            contents.add("  " + header + ": " + response.getHeader(header) + "\n");
        }
        return contents;
    }
}
