/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a constructor, field, setter method, or config method as to be autowired by
 * Spring's dependency injection facilities. This is an alternative to the JSR-330
 * {@link javax.inject.Inject} annotation, adding required-vs-optional semantics.
 * 将构造函数，字段，setter方法，或者config方法标记为由spring的依赖注入工具连接
 * 也可以选择JSR-330的注解Inject，添加必须或可选的语义
 *
 * <p>Only one constructor of any given bean class may declare this annotation with
 * the 'required' attribute set to {@code true}, indicating <i>the</i> constructor
 * to autowire when used as a Spring bean. Furthermore, if the 'required' attribute
 * is set to {@code true}, only a single constructor may be annotated with
 * {@code @Autowired}. If multiple <i>non-required</i> constructors declare the
 * annotation, they will be considered as candidates for autowiring. The constructor
 * with the greatest number of dependencies that can be satisfied by matching beans
 * in the Spring container will be chosen. If none of the candidates can be satisfied,
 * then a primary/default constructor (if present) will be used. If a class only
 * declares a single constructor to begin with, it will always be used, even if not
 * annotated. An annotated constructor does not have to be public.
 * 任意给定的bean类，只能有一个构造器上的@Autowired的required属性，设置为true。表明这个构造器是用来注入的
 * 此外，如果设置required属性为true,只有一个构造器可以用注解 @Autowired。如果多个required为false的@Autowired，
 * 这些构造器将会作为候选者。
 * 构造器里面的参数依赖的bean越多的，将会被选中。
 * 如果没有一个候选者被选中，就选默认构造器
 * 如果一个类只声明了一个构造器，那么就选这个构造器，即使没有加上注解@Autowired
 * 构造器不必为public类型
 * 
 * 
 * 
 *
 * <p>Fields are injected right after construction of a bean, before any config methods
 * are invoked. Such a config field does not have to be public.
 * 字段是在bean的构造方法之后，在任意配置方法被调用之前注入的。这样的配置字段不必是public类型的
 * 
 * <p>Config methods may have an arbitrary name and any number of arguments; each of
 * those arguments will be autowired with a matching bean in the Spring container.
 * Bean property setter methods are effectively just a special case of such a general
 * config method. Such config methods do not have to be public.
 * 配置方法可能有一个任意的名称，和任意数量的参数；那些参数每一个都会和spring容器中的bean匹配，并注入进来
 * Bean属性的setter方法只是这类通用配置方法的特例。这类配置方法不必是public类型
 *
 * <p>In the case of a multi-arg constructor or method, the 'required' attribute is
 * applicable to all arguments. Individual parameters may be declared as Java-8-style
 * {@link java.util.Optional} or, as of Spring Framework 5.0, also as {@code @Nullable}
 * or a not-null parameter type in Kotlin, overriding the base required semantics.
 * 如果是一个多参数的构造器或方法，required属性适用于所有参数。单个参数覆盖掉基本的required语义，可能需要声明成Java8类型的Optional
 * 或者，在spring5中，声明成@Nullable， 或者，在Kotlin中，声明成not-null参数类型。
 *
 * <p>In case of a {@link java.util.Collection} or {@link java.util.Map} dependency type,
 * the container autowires all beans matching the declared value type. For such purposes,
 * the map keys must be declared as type String which will be resolved to the corresponding
 * bean names. Such a container-provided collection will be ordered, taking into account
 * {@link org.springframework.core.Ordered}/{@link org.springframework.core.annotation.Order}
 * values of the target components, otherwise following their registration order in the
 * container. Alternatively, a single matching target bean may also be a generally typed
 * {@code Collection} or {@code Map} itself, getting injected as such.
 * 如果是Collection或者Map类型的依赖，容器自动注入所有匹配了声明类型的bean。因此，map的key必须声明为string，这样是为了解决相关的bean名称。
 * 这样的容器提供的集合，是根据Ordered注解的值来排序，或者根据他们在容器中注册的顺序来排序
 * 另外，单个匹配的bean也可能是Collection获Map对象，
 * 
 *
 * <p>Note that actual injection is performed through a
 * {@link org.springframework.beans.factory.config.BeanPostProcessor
 * BeanPostProcessor} which in turn means that you <em>cannot</em>
 * use {@code @Autowired} to inject references into
 * {@link org.springframework.beans.factory.config.BeanPostProcessor
 * BeanPostProcessor} or
 * {@link org.springframework.beans.factory.config.BeanFactoryPostProcessor BeanFactoryPostProcessor}
 * types. Please consult the javadoc for the {@link AutowiredAnnotationBeanPostProcessor}
 * class (which, by default, checks for the presence of this annotation).
 * 注意，实际注入是通过BeanPostProcessor来执行的，继而意味着你不能通过@Autowired来注入一个引用到BeanPostProcessor中，
 * 或者BeanFactoryPostProcessor中
 * 
 * AutowiredAnnotationBeanPostProcessor类是用来检查是否存在Autowired注解
 *
 * 重点单词：inject注入
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @since 2.5
 * @see AutowiredAnnotationBeanPostProcessor
 * @see Qualifier
 * @see Value
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

	/**
	 * Declares whether the annotated dependency is required.
	 * <p>Defaults to {@code true}.
	 * 
	 * 声明这个注解的依赖是否必须的
	 * 默认是必须的。
	 * 也就是，引入不了这个bean,就会报错
	 */
	boolean required() default true;

}
