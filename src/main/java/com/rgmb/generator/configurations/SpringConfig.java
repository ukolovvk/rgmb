package com.rgmb.generator.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Конфигурационный класс - подключаются необходимые аннотации
 */

@Configuration
@ComponentScan(basePackages = "com.rgmb.generator")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
public class SpringConfig  {}
