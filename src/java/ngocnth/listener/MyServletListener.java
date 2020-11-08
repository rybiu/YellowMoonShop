/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Web application lifecycle listener.
 *
 * @author Ruby
 */
public class MyServletListener implements ServletContextListener {
    
    private static final Logger LOGGER = LogManager.getLogger(MyServletListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String realPath = context.getRealPath("/") + "WEB-INF\\"; // + WEB-INF -- doc file sitMap
        String siteMapPath = realPath + "siteMap.txt";
        String nonAuthSiteMapPath = realPath + "nonAuthSiteMap.txt";
        Map<String, String> siteMap;
        Map<String, String> nonAuthSiteMap;
        try {
            siteMap = this.loadMapFromFile(siteMapPath);
            nonAuthSiteMap = this.loadMapFromFile(nonAuthSiteMapPath);
            context.setAttribute("SITE_MAP", siteMap);
            context.setAttribute("NON_AUTH_SITE_MAP", nonAuthSiteMap);
        } catch (IOException ex) {
            LOGGER.error("IOException", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private Map<String, String> loadMapFromFile(String filePath)
            throws IOException {
        Map<String, String> map = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            File f = new File(filePath);
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String line;
            map = new HashMap<>();
            while ((line = br.readLine()) != null) {
                StringTokenizer stk = new StringTokenizer(line, "=");
                String key = stk.nextToken().trim();
                String value = stk.nextToken().trim();
                map.put(key, value);
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
        }
        return map;
    }
}
