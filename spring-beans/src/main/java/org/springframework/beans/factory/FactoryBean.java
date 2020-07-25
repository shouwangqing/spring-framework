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

package org.springframework.beans.factory;

import org.springframework.lang.Nullable;

/**
 * Interface to be implemented by objects used within a {@link BeanFactory} which
 * are themselves factories for individual objects. If a bean implements this
 * interface, it is used as a factory for an object to expose, not directly as a
 * bean instance that will be exposed itself.
 * 由BeanFactory使用的对象来实现的接口，它们本身就是单个对象的工厂。如果一个bean实现了这个接口，它就作为一个对象暴露的一个工厂，
 * 而不是直接作为一个bean实例来暴露自己
 *
 * <p><b>NB: A bean that implements this interface cannot be used as a normal bean.</b>
 * A FactoryBean is defined in a bean style, but the object exposed for bean
 * references ({@link #getObject()}) is always the object that it creates.
 * 一个bean实现这个接口，但不能作为一个普通的bean
 * 一个FactoryBean是以bean的样式来定义的，但是暴露给bean引用（getObject()）的对象总是它创建的对象
 *
 * <p>FactoryBeans can support singletons and prototypes, and can either create
 * objects lazily on demand or eagerly on startup. The {@link SmartFactoryBean}
 * interface allows for exposing more fine-grained behavioral metadata.
 * 
 * FactoryBeans可支持单例，或原型模型，既可以按需懒创建对象，也可以在启动时饥饿创建对象
 * SmartFactoryBean接口允许暴露更多细粒度的行为数据
 *
 * <p>This interface is heavily used within the framework itself, for example for
 * the AOP {@link org.springframework.aop.framework.ProxyFactoryBean} or the
 * {@link org.springframework.jndi.JndiObjectFactoryBean}. It can be used for
 * custom components as well; however, this is only common for infrastructure code.
 * 
 * 这个接口在框架本身中被大量的使用，例如AOP中（org.springframework.aop.framework.ProxyFactoryBean）
 * 或者org.springframework.jndi.JndiObjectFactoryBean
 * 它也可以用来自定义组件；但是这只是基础架构代码中常见的情况
 *
 * <p><b>{@code FactoryBean} is a programmatic contract. Implementations are not
 * supposed to rely on annotation-driven injection or other reflective facilities.</b>
 * {@link #getObjectType()} {@link #getObject()} invocations may arrive early in
 * the bootstrap process, even ahead of any post-processor setup. If you need access
 * other beans, implement {@link BeanFactoryAware} and obtain them programmatically.
 * 
 * FactoryBean是一个编程式契约。不建议实现依赖注解驱动注入，或其他反射场所
 * getObjectType()，getObject()调用可能会在启动流程的早期到达，甚至可能在任何后置处理器启动之前。
 * 如果你需要访问其他beans，实现BeanFactoryAware，并通过编程式获得它们（（（重点）））
 *
 * <p>Finally, FactoryBean objects participate in the containing BeanFactory's
 * synchronization of bean creation. There is usually no need for internal
 * synchronization other than for purposes of lazy initialization within the
 * FactoryBean itself (or the like).
 * 
 * 最后，FactoryBean对象参与容器BeanFactory的bean创建的同步
 * 通常不需要内部同步，但不是为了FactoryBean本身（或类似）的懒初始化
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 08.03.2003
 * @param <T> the bean type
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.aop.framework.ProxyFactoryBean
 * @see org.springframework.jndi.JndiObjectFactoryBean
 */
public interface FactoryBean<T> {

	/**
	 * Return an instance (possibly shared or independent) of the object
	 * managed by this factory.
	 * <p>As with a {@link BeanFactory}, this allows support for both the
	 * Singleton and Prototype design pattern.
	 * <p>If this FactoryBean is not fully initialized yet at the time of
	 * the call (for example because it is involved in a circular reference),
	 * throw a corresponding {@link FactoryBeanNotInitializedException}.
	 * <p>As of Spring 2.0, FactoryBeans are allowed to return {@code null}
	 * objects. The factory will consider this as normal value to be used; it
	 * will not throw a FactoryBeanNotInitializedException in this case anymore.
	 * FactoryBean implementations are encouraged to throw
	 * FactoryBeanNotInitializedException themselves now, as appropriate.
	 * @return an instance of the bean (can be {@code null})
	 * @throws Exception in case of creation errors
	 * @see FactoryBeanNotInitializedException
	 * 
	 * 返回由这个工厂管理的对象的一个实例（可能是共享的，或独立的）
	 * 与BeanFactory一样，这允许同时支持单例和原型设计模式
	 * 如果这个FactoryBean在调用阶段还没有完全初始化（例如因为它卷入到一个循环引用中），则会抛出相应的异常FactoryBeanNotInitializedException
	 * 从Spring2.0开始，FactoryBeans允许返回null对象。工厂将考虑将此作为一个常用值使用；它在这种情况下不再抛出异常FactoryBeanNotInitializedException 
	 * 视情况而定，现在鼓励FactoryBean实现他们本身抛出FactoryBeanNotInitializedException，
	 * 
	 */
	@Nullable
	T getObject() throws Exception;

	/**
	 * Return the type of object that this FactoryBean creates,
	 * or {@code null} if not known in advance.
	 * <p>This allows one to check for specific types of beans without
	 * instantiating objects, for example on autowiring.
	 * <p>In the case of implementations that are creating a singleton object,
	 * this method should try to avoid singleton creation as far as possible;
	 * it should rather estimate the type in advance.
	 * For prototypes, returning a meaningful type here is advisable too.
	 * <p>This method can be called <i>before</i> this FactoryBean has
	 * been fully initialized. It must not rely on state created during
	 * initialization; of course, it can still use such state if available.
	 * <p><b>NOTE:</b> Autowiring will simply ignore FactoryBeans that return
	 * {@code null} here. Therefore it is highly recommended to implement
	 * this method properly, using the current state of the FactoryBean.
	 * @return the type of object that this FactoryBean creates,
	 * or {@code null} if not known at the time of the call
	 * @see ListableBeanFactory#getBeansOfType
	 * 
	 * 返回这个FactoryBean所创建对象的类型，或者如果事先不知道的话则null
	 * 这个允许在没有初始化对象的时候，检查bean的特定类型，例如autowiring
	 * 如果这个实现是创建一个单例对象，那么这个方法应该尽量避免单例的创建；它应该提前估计类型
	 * 对于原型，返回一个有意义的类型也是可取的
	 * 这个方法可以在这个FactoryBean完全初始化之前被调用。
	 * 它不能依赖于初始化期间创建的状态；当然，如果可用，它仍然可以继续这个状态
	 * 
	 * 注意：在这里Autowiring仅会忽略FactoryBeans返回的null。
	 * 因此，强烈推荐使用FactoryBean当前的状态，适当的实现这个方法，
	 * 
	 */
	@Nullable
	Class<?> getObjectType();

	/**
	 * Is the object managed by this factory a singleton? That is,
	 * will {@link #getObject()} always return the same object
	 * (a reference that can be cached)?
	 * <p><b>NOTE:</b> If a FactoryBean indicates to hold a singleton object,
	 * the object returned from {@code getObject()} might get cached
	 * by the owning BeanFactory. Hence, do not return {@code true}
	 * unless the FactoryBean always exposes the same reference.
	 * <p>The singleton status of the FactoryBean itself will generally
	 * be provided by the owning BeanFactory; usually, it has to be
	 * defined as singleton there.
	 * <p><b>NOTE:</b> This method returning {@code false} does not
	 * necessarily indicate that returned objects are independent instances.
	 * An implementation of the extended {@link SmartFactoryBean} interface
	 * may explicitly indicate independent instances through its
	 * {@link SmartFactoryBean#isPrototype()} method. Plain {@link FactoryBean}
	 * implementations which do not implement this extended interface are
	 * simply assumed to always return independent instances if the
	 * {@code isSingleton()} implementation returns {@code false}.
	 * <p>The default implementation returns {@code true}, since a
	 * {@code FactoryBean} typically manages a singleton instance.
	 * @return whether the exposed object is a singleton
	 * @see #getObject()
	 * @see SmartFactoryBean#isPrototype()
	 * 
	 * 这个工厂管理的对象是单例的吗？这样，getObject()总会返回同样的对象（一个引用也会被缓存）？
	 * 
	 * 注意：如果一个FactoryBean指示持有一个单例对象，这个对象从getObject()中返回时，可能会从自己所拥有的BeanFactory的获得缓存
	 * 因此，不要返回true，除非这个FactoryBean总是暴露相同的引用。
	 * FactoryBean本身得的单例状态通常是由它自己拥有的BeanFactory提供的
	 * 通常，它不得不在这里定义成单例
	 * 
	 * 注意：这个方法返回false将不会不一定指示返回的对象是独立的实例。
	 * 扩展接口SmartFactoryBean的实现可能会通过它的isPrototype()方法显式指示独立实例
	 * 没有实现这个扩展接口的FactoryBean普通实现，如果isSingleton()实现返回false,将会总是返回独立实例。
	 * 默认实现返回true，因为FactoryBean通常管理一个单例实例
	 */
	default boolean isSingleton() {
		return true;
	}

}
