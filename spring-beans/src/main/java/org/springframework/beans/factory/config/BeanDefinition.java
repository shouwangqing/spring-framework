/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;
import org.springframework.lang.Nullable;

/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 * 一个bean定义，描述了一个bean的接口，bean定义可能有属性值，构造器参数值，还有更多具体实现的信息
 *
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} such as {@link PropertyPlaceholderConfigurer}
 * to introspect and modify property values and other bean metadata.
 * 这仅仅是一个最小的接口：主要的目的是允许BeanFactoryPostProcessor，例如PropertyPlaceholderConfigurer
 * 去自省并修改属性值和其他bean元数据
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 * 标准单例的作用域的作用域标识符：singleton
	 * 注意：扩展bean工厂可能会支持更多的作用域
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 * 标准原型作用域的作用域标识符：prototype
	 * 注意：扩展bean工厂可能会支持更多的作用域
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


	/**
	 * Role hint indicating that a {@code BeanDefinition} is a major part
	 * of the application. Typically corresponds to a user-defined bean.
	 * 该角色表示，BeanDefinition是应用中的一个主要部分。
	 * 通常跟用户定义的bean相对应
	 */
	int ROLE_APPLICATION = 0;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is a supporting
	 * part of some larger configuration, typically an outer
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * {@code SUPPORT} beans are considered important enough to be aware
	 * of when looking more closely at a particular
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
	 * but not when looking at the overall configuration of an application.
	 * 
	 * 该角色表示，BeanDefinition支持一些大型配置的某些部分，通常是外部的org.springframework.beans.factory.parsing.ComponentDefinition
	 * 当更仔细的看待特定的org.springframework.beans.factory.parsing.ComponentDefinition
	 * SUPPORT bean被认为足够重要的
	 * 当看待一个应用中的所有配置的时候，就不是这样了
	 * 
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is providing an
	 * entirely background role and has no relevance to the end-user. This hint is
	 * used when registering beans that are completely part of the internal workings
	 * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * 该角色表示，BeanDefinition提供一个完整的后台角色，并且与终端用户无关
	 * 这表示当注册bean时，它完全是org.springframework.beans.factory.parsing.ComponentDefinition
	 * 的内部工作的部分
	 */
	int ROLE_INFRASTRUCTURE = 2;


	// Modifiable attributes

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 * 设置这个bean定义的父级定义的名称（如果有）
	 */
	void setParentName(@Nullable String parentName);

	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 * 返回这个bean定义的父级定义的名称（如果有）
	 */
	@Nullable
	String getParentName();

	/**
	 * Specify the bean class name of this bean definition.
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 * @see #setParentName
	 * @see #setFactoryBeanName
	 * @see #setFactoryMethodName
	 * 
	 * 指定这个bean定义的bean类名称
	 * 这个类类名可以在bean工厂的后置处理中被修改
	 * 通常用一个以解析的变量替换这个原始的类名称
	 */
	void setBeanClassName(@Nullable String beanClassName);

	/**
	 * Return the current bean class name of this bean definition.
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Also, this may just be the class that a factory method is called on, or it may
	 * even be empty in case of a factory bean reference that a method is called on.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 * @see #getParentName()
	 * @see #getFactoryBeanName()
	 * @see #getFactoryMethodName()
	 * 
	 * 返回这个bean定义的当前bean类名称
	 * 注意：如果一个子定义重写或继承了父类的类名，那么在运行期间，这个不一定是实际的类名，
	 * 同样，这个可能仅仅是一个工厂方法所调用的类，或者一个工厂的bean引用的情况下，一个方法被调用，它可能为空的，
	 * 因此，不要考虑这个在运行时为定义的bean类型，但是相反，它仅用在单个bean定义级别上进行解析
	 */
	@Nullable
	String getBeanClassName();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 * 重写这个bean的目标作用域，指定一个新的作用域名称
	 */
	void setScope(@Nullable String scope);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 * 返回这个bean的当前目标作用域的名称
	 */
	@Nullable
	String getScope();

	/**
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 * 设置这个bean是否延迟初始化
	 * 如果设置为false, 这个bean在bean工厂启动时初始化，它执行了单例的饥饿初始化
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 * 返回这个bean是否懒初始化，例如，在启动时不是饥饿初始化
	 * 仅应用在一个单例bean
	 */
	boolean isLazyInit();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 * 设置此bean依赖于初始化的bean名称
	 * bean工厂会保证首先初始化这些bean
	 */
	void setDependsOn(@Nullable String... dependsOn);

	/**
	 * Return the bean names that this bean depends on.
	 * 返回这个bean所依赖的bean名称
	 */
	@Nullable
	String[] getDependsOn();

	/**
	 * Set whether this bean is a candidate for getting autowired into some other bean.
	 * <p>Note that this flag is designed to only affect type-based autowiring.
	 * It does not affect explicit references by name, which will get resolved even
	 * if the specified bean is not marked as an autowire candidate. As a consequence,
	 * autowiring by name will nevertheless inject a bean if the name matches.
	 * 
	 * 设置此bean是否为获取自动装配到其他bean的一个候选对象
	 * 注意，这个标记是为了仅会受到基于类型自动装配影响而设计的
	 * 它不会影响通过名称的显式引用，尽管所指定的bean不标记为自动装配的候选对象，它也可以解决
	 * 因此，如果名称匹配，则依然通过名称注入一个bean
	 * 
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 * 返回这个bean是否为获取自动装配到其他bean的一个候选对象
	 */
	boolean isAutowireCandidate();

	/**
	 * Set whether this bean is a primary autowire candidate.
	 * <p>If this value is {@code true} for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 * 设置这个bean是否为主要的自动装配候选对象
	 * 如果这个值在多个匹配的候选对象中的某个bean设为true，
	 * 则它将会作为一个
	 */
	void setPrimary(boolean primary);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 * 返回这个bean是否为主要的自动装配候选对象
	 */
	boolean isPrimary();

	/**
	 * Specify the factory bean to use, if any.
	 * This the name of the bean to call the specified factory method on.
	 * @see #setFactoryMethodName
	 * 指定所使用的工厂bean，如果有的话
	 * 这是bean的名称，来调用特殊的工厂方法
	 */
	void setFactoryBeanName(@Nullable String factoryBeanName);

	/**
	 * Return the factory bean name, if any.
	 * 返回工厂bean的名称，如果有的话
	 */
	@Nullable
	String getFactoryBeanName();

	/**
	 * Specify a factory method, if any. This method will be invoked with
	 * constructor arguments, or with no arguments if none are specified.
	 * The method will be invoked on the specified factory bean, if any,
	 * or otherwise as a static method on the local bean class.
	 * @see #setFactoryBeanName
	 * @see #setBeanClassName
	 * 
	 * 指定一个工厂方法（如果有）。
	 * 这个方法会引用构造器参数，或者如果没有指定的话，就没有参数
	 * 这个方法会被特定的工厂bean所引用，如果有的话
	 */
	void setFactoryMethodName(@Nullable String factoryMethodName);

	/**
	 * Return a factory method, if any.
	 * 返回工厂方法，如果有
	 */
	@Nullable
	String getFactoryMethodName();

	/**
	 * Return the constructor argument values for this bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the ConstructorArgumentValues object (never {@code null})
	 * 返回这个bean的构造器参数值
	 * 这个返回的实例可以在bean工厂的后置处理过程中修改
	 * 
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * Return if there are constructor argument values defined for this bean.
	 * @since 5.0.2
	 * 返回是否有为这个bean定义构造器参数值
	 */
	default boolean hasConstructorArgumentValues() {
		return !getConstructorArgumentValues().isEmpty();
	}

	/**
	 * Return the property values to be applied to a new instance of the bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the MutablePropertyValues object (never {@code null})
	 * 
	 * 返回应用到一个新的bean的实例中的属性值
	 * 这个返回的实例可以在bean工厂的后置处理过程中修改
	 */
	MutablePropertyValues getPropertyValues();

	/**
	 * Return if there are property values values defined for this bean.
	 * @since 5.0.2
	 * 返回是否有为这个bean设置属性值
	 */
	default boolean hasPropertyValues() {
		return !getPropertyValues().isEmpty();
	}

	/**
	 * Set the name of the initializer method.
	 * @since 5.1
	 * 为初始化方法设置名称
	 */
	void setInitMethodName(@Nullable String initMethodName);

	/**
	 * Return the name of the initializer method.
	 * @since 5.1
	 * 获取初始化方法的名称
	 */
	@Nullable
	String getInitMethodName();

	/**
	 * Set the name of the destroy method.
	 * @since 5.1
	 * 为销毁方法设置名称
	 */
	void setDestroyMethodName(@Nullable String destroyMethodName);

	/**
	 * Return the name of the destroy method.
	 * @since 5.1
	 * 返回销毁方法的名称
	 */
	@Nullable
	String getDestroyMethodName();

	/**
	 * Set the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools with an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @since 5.1
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 * 
	 * 为这个bean定义设置角色提示
	 * 角色提示为框架和工具提供角色标识，和特别重要的BeanDefinition
	 */
	void setRole(int role);

	/**
	 * Get the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools with an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 * 
	 * 获取这个bean定义的角色提示
	 * 角色提示为框架和工具提供角色标识，和特别重要的BeanDefinition
	 */
	int getRole();

	/**
	 * Set a human-readable description of this bean definition.
	 * @since 5.1
	 * 设置这个bean定义的可读描述
	 */
	void setDescription(@Nullable String description);

	/**
	 * Return a human-readable可读 description of this bean definition.
	 * 返回这个bean定义的可读描述
	 */
	@Nullable
	String getDescription();


	// Read-only attributes

	/**
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 * @see #SCOPE_SINGLETON
	 * 返回这个是否为单例的，在所有的调用中都返回共享实例
	 */
	boolean isSingleton();

	/**
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * @since 3.0
	 * @see #SCOPE_PROTOTYPE
	 * 
	 * 返回这个是否为原型，对于每一次调用都返回独立的实例
	 */
	boolean isPrototype();

	/**
	 * Return whether this bean is "abstract", that is, not meant to be instantiated.
	 * 返回这个bean是否为抽象的，并不是意味着要初始化
	 */
	boolean isAbstract();

	/**
	 * Return a description of the resource that this bean definition
	 * came from (for the purpose of showing context in case of errors).
	 * 返回这个bean定义来源的资源的描述（为了展示上下文，如果有错的话）
	 */
	@Nullable
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * Allows for retrieving the decorated修饰 bean definition, if any.
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 * 返回原始BeanDefinition， 如果没有的话就null， 
	 * 允许检索被修饰的bean定义（如果有）
	 * 注意，这个方法返回的是当前的原始的
	 * 迭代初始化链来查找原始的由用户定义的BeanDefinition
	 */
	@Nullable
	BeanDefinition getOriginatingBeanDefinition();

}
