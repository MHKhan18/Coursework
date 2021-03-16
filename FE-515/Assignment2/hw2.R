

# Question-1


seed <- 1

rnd <- function(n){
  m <- 244944
  a <- 1597
  b <- 51749
  x <- rep(NA , n)
  x[1] <- (a * seed + b) %% m
  for(i in 1:(n-1)){
    x[i+1] <- (a * x[i] + b) %% m
  }
  
  seed <<- x[n] # <<- for global change
  return(x/m)
}

U <- rnd(10000)
X <- qchisq(U , df=10)

hist(X , nclass = 40)


# Question-2

seed <- as.numeric(Sys.time())
N <- 10000
x <- rnd(N)
y <- rnd(N)
z <- rnd(N)

num_inside <- sum(x^2 + y^2 + z^2 <= 1)
area_quarter_sphere <- num_inside/N
volume <- 8 * area_quarter_sphere
volume


#) Question - 3


JPM <- read.csv("JPM.csv")

date <- JPM$Date
date <- as.Date(date)
JPM$Date <- date

par(mfcol = c(2, 2)) # graphics setting

plot(JPM$Date , JPM$Adj.Close , type="b" , 
     xlim = c(as.numeric(min(JPM$Date)),as.numeric(max(JPM$Date))),
     ylim = c(min(JPM$Adj.Close) , max(JPM$Adj.Close)),
     main = "JPM" , xlab = "Date" , 
     ylab = "Adjusted Close Price" , col = "red"
     )

plot(JPM$Open , JPM$Close,
     xlim = c(min(JPM$Open) , max(JPM$Open)),
     ylim = c(min(JPM$Close) , max(JPM$Close)),
     xlab = "Open Price" , ylab = "Close price"
     )


price_intervals = cut(JPM$Adj.Close , breaks = 4)
barplot(table(price_intervals), 
        xlab = "Adjusted Close Price",
        ylab = "Frequency"
        )


boxplot( JPM$Volume ~ price_intervals, las = 1)
        


