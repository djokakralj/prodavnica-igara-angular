package com.djole.prodavnica.config;

import com.djole.prodavnica.web.mvc.AdminAuthFilter;
import com.djole.prodavnica.web.mvc.AdminDashboardServlet;
import com.djole.prodavnica.web.mvc.AdminIgreServlet;
import com.djole.prodavnica.web.mvc.AdminIzdavaciServlet;
import com.djole.prodavnica.web.mvc.AdminKupciServlet;
import com.djole.prodavnica.web.mvc.AdminNarudzbineServlet;
import com.djole.prodavnica.web.mvc.AdminPlatformeServlet;
import com.djole.prodavnica.web.mvc.LoginServlet;
import com.djole.prodavnica.web.mvc.LogoutServlet;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
        return filter(new OpenEntityManagerInViewFilter(), 1, "/api/*", "/admin/*", "/login");
    }

    @Bean
    public FilterRegistrationBean<AdminAuthFilter> adminAuthFilter() {
        return filter(new AdminAuthFilter(), 2, "/admin/*");
    }

    @Bean
    public ServletRegistrationBean<LoginServlet> loginServlet() {
        return servlet(new LoginServlet(), "/login", "");
    }

    @Bean
    public ServletRegistrationBean<LogoutServlet> logoutServlet() {
        return servlet(new LogoutServlet(), "/logout");
    }

    @Bean
    public ServletRegistrationBean<AdminDashboardServlet> adminDashboardServlet() {
        return servlet(new AdminDashboardServlet(), "/admin");
    }

    @Bean
    public ServletRegistrationBean<AdminIgreServlet> adminIgreServlet() {
        return servlet(new AdminIgreServlet(), "/admin/igre");
    }

    @Bean
    public ServletRegistrationBean<AdminIzdavaciServlet> adminIzdavaciServlet() {
        return servlet(new AdminIzdavaciServlet(), "/admin/izdavaci");
    }

    @Bean
    public ServletRegistrationBean<AdminPlatformeServlet> adminPlatformeServlet() {
        return servlet(new AdminPlatformeServlet(), "/admin/platforme");
    }

    @Bean
    public ServletRegistrationBean<AdminNarudzbineServlet> adminNarudzbineServlet() {
        return servlet(new AdminNarudzbineServlet(), "/admin/narudzbine");
    }

    @Bean
    public ServletRegistrationBean<AdminKupciServlet> adminKupciServlet() {
        return servlet(new AdminKupciServlet(), "/admin/kupci");
    }

    private static <T extends Filter> FilterRegistrationBean<T> filter(T filter, int redosled, String... putanje) {
        FilterRegistrationBean<T> registracija = new FilterRegistrationBean<>(filter);
        registracija.addUrlPatterns(putanje);
        registracija.setOrder(redosled);
        return registracija;
    }

    private static <T extends HttpServlet> ServletRegistrationBean<T> servlet(T servlet, String... putanje) {
        ServletRegistrationBean<T> registracija = new ServletRegistrationBean<>(servlet, putanje);
        registracija.setLoadOnStartup(1);
        return registracija;
    }
}
