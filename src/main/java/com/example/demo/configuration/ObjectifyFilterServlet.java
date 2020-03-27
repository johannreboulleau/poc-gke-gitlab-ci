package com.example.demo.configuration;

import com.googlecode.objectify.ObjectifyFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ObjectifyFilterServlet extends ObjectifyFilter {
}
