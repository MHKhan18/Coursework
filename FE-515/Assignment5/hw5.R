
# Question 1
# ===================

#Q 1.1

Cov <- matrix(c(0.01 , 0.002 , 0.001 , 0.002 , 0.011 , 0.003 , 0.001 , 0.003 , 0.02) , 3 , 3 , byrow =TRUE)

f <- function(x) 0.5*t(x)%*%Cov%*%x

ui <- matrix(c(0.0427 , 0.0015 , 0.0285 , -1 , -1 , -1) , 2, 3, byrow =TRUE)
ci <- c(0.05 , -1)

constrOptim(c(2,-2,0), f , grad = NULL, ui = ui, ci = ci)$par
constrOptim(c(2,-2,0), f , grad = NULL, ui = ui, ci = ci)$value

# Q 1.2
#install.packages("quadprog")
library(quadprog)
D <- Cov
d <- c(0 , 0 , 0)
A <- matrix(c(0.0427 , 0.0015 , 0.0285 , -1 , -1 , -1) , 3, 2) # by column
b <- c(0.05 , -1)
solve.QP(D, d, A, b)$solution
solve.QP(D, d, A, b)$value


#Question 2
#====================================

x <- c(0.25 , 0.5 , 1 , 2, 3, 5, 7, 10)
y <- c(0.09 , 0.11 , 0.16 , 0.20 , 0.24 , 0.36 , 0.53 , 0.64)
x.out <- c(0.75 , 1.5 , 4 , 6 , 8)

# linear interpolation
yout <- approx(x,y,xout = x.out)$y
yout

# spline interpolation
yout.s <- spline(x,y, xout = x.out, method = "natural")
yout.s$y



# Question - 3
# ==========================

S0 = 100
K = 100
T1 = 1
sigma = 0.2
r = 0.05

bs.call.raw <- function(x) (S0 * exp((r - 0.5*sigma^2)*T1 + sigma*sqrt(T1)*x) - K) * dnorm(x)
  
d2 <- (log(S0/K) + (r-0.5*sigma^2)*T1)/(sigma*sqrt(T1))

integral <- integrate(bs.call.raw , -d2 , Inf)$value

res <- exp(-r*T1) * integral
res




