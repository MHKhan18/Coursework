
# Question-1

# Q 1.1
bs.call <- function(S0, K, T1, sigma, r , type){
  
  d1 <- (log(S0/K) + (r+0.5*sigma^2)*T1)/(sigma*sqrt(T1))
  d2 <- d1 - sigma*sqrt(T1)
  
  if(type == "call"){
    return(S0*pnorm(d1) - exp(-r*T1)*K*pnorm(d2))
  }
  return(-S0*pnorm(-d1) + exp(-r*T1)*K*pnorm(-d2)) # put option
}

S0 = 100
K = 100
T1 = 1
sigma = 0.2
r = 0.05

bs.call(S0 , K , T1 , sigma , r, "call")
bs.call(S0 , K , T1 , sigma , r, "put")

# Q 1.2
bisection.new <- function(f, a, b, tol = 0.001, N.max = 100){
  f.a <- f(a)
  f.b <- f(b)
  if(is.na(f.a*f.b) || f.a*f.b > 0){# only modified this part
    return(NA)
  }else if(f.a == 0){
    return(a)
  }else if(f.b == 0){
    return(b)
  }
  for(n in 1:N.max){
    c <- (a+b)/2
    f.c <- f(c)
    if(f.c == 0 || abs(b - a) < tol){
      break
    }
    if(f.a*f.c < 0){
      b <- c
      f.b <- f.c
    }else{
      a <- c
      f.a <- f.c
    }
  }
  return(c)
}


implied.vol <- function(S0, K, T1, r, price , type){
  price.diff <- function(sigma)bs.call(S0, K, T1, sigma, r , type) - price
  return(bisection.new(price.diff, 0.01, 5))
}

implied.vol(S0 , K , T1 , r , 10 , "call")
implied.vol(S0 , K , T1 , r , 5 , "put")


# Question 2
# =======================================

n <- 252
m <- 10000
h <- T1/n
MC <- function(m){
  
  S.vec <- rep(S0, m)
  Z <- matrix(rnorm(n*m), nrow = n)
  
  for (i in 1:n) {
    S.vec <- S.vec + r*S.vec*h + sigma*S.vec*Z[i,]*sqrt(h)
  }
  
  exp(-r*T1)*mean(pmax(K - S.vec, 0))
  
}

MC(m)

