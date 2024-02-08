package com.phoenix.amazon.AmazonBackend.helpers;

@FunctionalInterface
public interface TriPredicate<T,U,V>{
    boolean test(final T t,final U u,final V v);
    default TriPredicate<T,U,V> negate(){
        return (t, u, v)-> !test(t,u,v);
    }
    default TriPredicate<T,U,V> and(TriPredicate<T,U,V> tp){
        return (t, u, v)-> tp.test(t,u,v) && test(t,u,v);
    }
    default TriPredicate<T,U,V> or(TriPredicate<T,U,V> tp){
        return (t, u, v)-> tp.test(t,u,v) || test(t,u,v);
    }
}