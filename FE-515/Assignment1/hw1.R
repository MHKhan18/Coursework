

#) Question 1

#) 1)
x <- seq(5, 35, by=2)
x
length(x)

#) 2)
A <- matrix(x, nrow = 4, ncol = 4, byrow = T)
A

#) 3)
eigen(A)$values

#) 4) 
A[c(1, 2), c(1, 2)] <- 7
A

#) 5)
det(A)

#) 6)
solve(A)

#) 7)
b <- A[1,]
b

#) 8)
y <- solve(A, b)
y

#) 9)
pmin(y, pi/2)

#) 10)
diag(seq(1, 10, by = 1))


#) Question 2


S <- rep(NA, 51)
S[1] <- 0
S[2] <- 1
for(n in 3:51){
  S[n] <- S[n-1] + S[n-2]
}
S[4] # S3
S[51] # S50


#) Question 3

for(i in 1:100){
  if (i%%3 == 0 && i%%5 == 0){
    print(i)
  }
}


#) Question 4

ThreeFiveMultiples <- function(n){
  vec <- c()
  i <- 1
  while(i <= n){
    if (i%%3 == 0 && i%%5 == 0){
      vec <- c(vec, i)
    }
    i <- i + 1
  }
  return(vec)
}

ThreeFiveMultiples(100)
ThreeFiveMultiples(200)


#) Question 5

MinDivisor <- function(a, b){
  i <- max(a, b) + 1
  while(!(i %% a == 0 && i %% b == 0)){
    i <- i + 1
  }
  return(i)
}

MinDivisor(3, 5)
MinDivisor(6, 10)


#) Question 6

JPM <- read.csv("JPM.csv")
JPM

subtable <- data.frame(JPM$Open, JPM$High, JPM$Low, JPM$Close)
subtable

sapply(subtable, mean)
