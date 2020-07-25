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
 * һ��bean���壬������һ��bean�Ľӿڣ�bean�������������ֵ������������ֵ�����и������ʵ�ֵ���Ϣ
 *
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} such as {@link PropertyPlaceholderConfigurer}
 * to introspect and modify property values and other bean metadata.
 * �������һ����С�Ľӿڣ���Ҫ��Ŀ��������BeanFactoryPostProcessor������PropertyPlaceholderConfigurer
 * ȥ��ʡ���޸�����ֵ������beanԪ����
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
	 * ��׼��������������������ʶ����singleton
	 * ע�⣺��չbean�������ܻ�֧�ָ����������
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 * ��׼ԭ����������������ʶ����prototype
	 * ע�⣺��չbean�������ܻ�֧�ָ����������
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


	/**
	 * Role hint indicating that a {@code BeanDefinition} is a major part
	 * of the application. Typically corresponds to a user-defined bean.
	 * �ý�ɫ��ʾ��BeanDefinition��Ӧ���е�һ����Ҫ���֡�
	 * ͨ�����û������bean���Ӧ
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
	 * �ý�ɫ��ʾ��BeanDefinition֧��һЩ�������õ�ĳЩ���֣�ͨ�����ⲿ��org.springframework.beans.factory.parsing.ComponentDefinition
	 * ������ϸ�Ŀ����ض���org.springframework.beans.factory.parsing.ComponentDefinition
	 * SUPPORT bean����Ϊ�㹻��Ҫ��
	 * ������һ��Ӧ���е��������õ�ʱ�򣬾Ͳ���������
	 * 
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is providing an
	 * entirely background role and has no relevance to the end-user. This hint is
	 * used when registering beans that are completely part of the internal workings
	 * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * �ý�ɫ��ʾ��BeanDefinition�ṩһ�������ĺ�̨��ɫ���������ն��û��޹�
	 * ���ʾ��ע��beanʱ������ȫ��org.springframework.beans.factory.parsing.ComponentDefinition
	 * ���ڲ������Ĳ���
	 */
	int ROLE_INFRASTRUCTURE = 2;


	// Modifiable attributes

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 * �������bean����ĸ�����������ƣ�����У�
	 */
	void setParentName(@Nullable String parentName);

	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 * �������bean����ĸ�����������ƣ�����У�
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
	 * ָ�����bean�����bean������
	 * ���������������bean�����ĺ��ô����б��޸�
	 * ͨ����һ���Խ����ı����滻���ԭʼ��������
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
	 * �������bean����ĵ�ǰbean������
	 * ע�⣺���һ���Ӷ�����д��̳��˸������������ô�������ڼ䣬�����һ����ʵ�ʵ�������
	 * ͬ����������ܽ�����һ���������������õ��࣬����һ��������bean���õ�����£�һ�����������ã�������Ϊ�յģ�
	 * ��ˣ���Ҫ�������������ʱΪ�����bean���ͣ������෴���������ڵ���bean���弶���Ͻ��н���
	 */
	@Nullable
	String getBeanClassName();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 * ��д���bean��Ŀ��������ָ��һ���µ�����������
	 */
	void setScope(@Nullable String scope);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 * �������bean�ĵ�ǰĿ�������������
	 */
	@Nullable
	String getScope();

	/**
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 * �������bean�Ƿ��ӳٳ�ʼ��
	 * �������Ϊfalse, ���bean��bean��������ʱ��ʼ������ִ���˵����ļ�����ʼ��
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 * �������bean�Ƿ�����ʼ�������磬������ʱ���Ǽ�����ʼ��
	 * ��Ӧ����һ������bean
	 */
	boolean isLazyInit();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 * ���ô�bean�����ڳ�ʼ����bean����
	 * bean�����ᱣ֤���ȳ�ʼ����Щbean
	 */
	void setDependsOn(@Nullable String... dependsOn);

	/**
	 * Return the bean names that this bean depends on.
	 * �������bean��������bean����
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
	 * ���ô�bean�Ƿ�Ϊ��ȡ�Զ�װ�䵽����bean��һ����ѡ����
	 * ע�⣬��������Ϊ�˽����ܵ����������Զ�װ��Ӱ�����Ƶ�
	 * ������Ӱ��ͨ�����Ƶ���ʽ���ã�������ָ����bean�����Ϊ�Զ�װ��ĺ�ѡ������Ҳ���Խ��
	 * ��ˣ��������ƥ�䣬����Ȼͨ������ע��һ��bean
	 * 
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 * �������bean�Ƿ�Ϊ��ȡ�Զ�װ�䵽����bean��һ����ѡ����
	 */
	boolean isAutowireCandidate();

	/**
	 * Set whether this bean is a primary autowire candidate.
	 * <p>If this value is {@code true} for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 * �������bean�Ƿ�Ϊ��Ҫ���Զ�װ���ѡ����
	 * ������ֵ�ڶ��ƥ��ĺ�ѡ�����е�ĳ��bean��Ϊtrue��
	 * ����������Ϊһ��
	 */
	void setPrimary(boolean primary);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 * �������bean�Ƿ�Ϊ��Ҫ���Զ�װ���ѡ����
	 */
	boolean isPrimary();

	/**
	 * Specify the factory bean to use, if any.
	 * This the name of the bean to call the specified factory method on.
	 * @see #setFactoryMethodName
	 * ָ����ʹ�õĹ���bean������еĻ�
	 * ����bean�����ƣ�����������Ĺ�������
	 */
	void setFactoryBeanName(@Nullable String factoryBeanName);

	/**
	 * Return the factory bean name, if any.
	 * ���ع���bean�����ƣ�����еĻ�
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
	 * ָ��һ����������������У���
	 * ������������ù������������������û��ָ���Ļ�����û�в���
	 * ��������ᱻ�ض��Ĺ���bean�����ã�����еĻ�
	 */
	void setFactoryMethodName(@Nullable String factoryMethodName);

	/**
	 * Return a factory method, if any.
	 * ���ع��������������
	 */
	@Nullable
	String getFactoryMethodName();

	/**
	 * Return the constructor argument values for this bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the ConstructorArgumentValues object (never {@code null})
	 * �������bean�Ĺ���������ֵ
	 * ������ص�ʵ��������bean�����ĺ��ô���������޸�
	 * 
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * Return if there are constructor argument values defined for this bean.
	 * @since 5.0.2
	 * �����Ƿ���Ϊ���bean���幹��������ֵ
	 */
	default boolean hasConstructorArgumentValues() {
		return !getConstructorArgumentValues().isEmpty();
	}

	/**
	 * Return the property values to be applied to a new instance of the bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the MutablePropertyValues object (never {@code null})
	 * 
	 * ����Ӧ�õ�һ���µ�bean��ʵ���е�����ֵ
	 * ������ص�ʵ��������bean�����ĺ��ô���������޸�
	 */
	MutablePropertyValues getPropertyValues();

	/**
	 * Return if there are property values values defined for this bean.
	 * @since 5.0.2
	 * �����Ƿ���Ϊ���bean��������ֵ
	 */
	default boolean hasPropertyValues() {
		return !getPropertyValues().isEmpty();
	}

	/**
	 * Set the name of the initializer method.
	 * @since 5.1
	 * Ϊ��ʼ��������������
	 */
	void setInitMethodName(@Nullable String initMethodName);

	/**
	 * Return the name of the initializer method.
	 * @since 5.1
	 * ��ȡ��ʼ������������
	 */
	@Nullable
	String getInitMethodName();

	/**
	 * Set the name of the destroy method.
	 * @since 5.1
	 * Ϊ���ٷ�����������
	 */
	void setDestroyMethodName(@Nullable String destroyMethodName);

	/**
	 * Return the name of the destroy method.
	 * @since 5.1
	 * �������ٷ���������
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
	 * Ϊ���bean�������ý�ɫ��ʾ
	 * ��ɫ��ʾΪ��ܺ͹����ṩ��ɫ��ʶ�����ر���Ҫ��BeanDefinition
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
	 * ��ȡ���bean����Ľ�ɫ��ʾ
	 * ��ɫ��ʾΪ��ܺ͹����ṩ��ɫ��ʶ�����ر���Ҫ��BeanDefinition
	 */
	int getRole();

	/**
	 * Set a human-readable description of this bean definition.
	 * @since 5.1
	 * �������bean����Ŀɶ�����
	 */
	void setDescription(@Nullable String description);

	/**
	 * Return a human-readable�ɶ� description of this bean definition.
	 * �������bean����Ŀɶ�����
	 */
	@Nullable
	String getDescription();


	// Read-only attributes

	/**
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 * @see #SCOPE_SINGLETON
	 * ��������Ƿ�Ϊ�����ģ������еĵ����ж����ع���ʵ��
	 */
	boolean isSingleton();

	/**
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * @since 3.0
	 * @see #SCOPE_PROTOTYPE
	 * 
	 * ��������Ƿ�Ϊԭ�ͣ�����ÿһ�ε��ö����ض�����ʵ��
	 */
	boolean isPrototype();

	/**
	 * Return whether this bean is "abstract", that is, not meant to be instantiated.
	 * �������bean�Ƿ�Ϊ����ģ���������ζ��Ҫ��ʼ��
	 */
	boolean isAbstract();

	/**
	 * Return a description of the resource that this bean definition
	 * came from (for the purpose of showing context in case of errors).
	 * �������bean������Դ����Դ��������Ϊ��չʾ�����ģ�����д�Ļ���
	 */
	@Nullable
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * Allows for retrieving the decorated���� bean definition, if any.
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 * ����ԭʼBeanDefinition�� ���û�еĻ���null�� 
	 * ������������ε�bean���壨����У�
	 * ע�⣬����������ص��ǵ�ǰ��ԭʼ��
	 * ������ʼ����������ԭʼ�����û������BeanDefinition
	 */
	@Nullable
	BeanDefinition getOriginatingBeanDefinition();

}
