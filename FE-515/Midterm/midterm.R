
library(quantmod)


# Q 1.1

getSymbols("JPM")
jpm <- data.frame(JPM)
#head(jpm)

getSymbols("WFC")
wfc <- data.frame(WFC)
#head(wfc)
#tail(wfc)

jpm.price <- jpm$JPM.Adjusted
wfc.price <- wfc$WFC.Adjusted

jpm.rtn <- diff(log(jpm.price))
wfc.rtn <- diff(log(wfc.price))


#Q 1.2

lin.reg <- lm(jpm.rtn ~ wfc.rtn)
summary(lin.reg)

#Q 1.3

plot(jpm.rtn ~ wfc.rtn)
abline(lin.reg , col="red")

#Q 1.4

cheese <- read.csv("cheese.csv")
#head(cheese)

cheese.model <- lm(taste ~ acetic + h2s + lactic, data = cheese) # full model
null.model <- lm(taste ~ 1, data = cheese) # model with no factor

summary(cheese.model)
summary(null.model)


# Q 1.5

full.model.formula <- taste ~ acetic + h2s + lactic

# forward selection
step(null.model, full.model.formula, direction = "forward")

#backward selection
step(cheese.model, full.model.formula, direction = "backward")

# both direction
step(null.model, full.model.formula, direction = "both")

# All three selection methods give the same model: taste = c1 + c2*h2s + c3*lactic + e
# corresponding regression line is: taste = -27.592 + 3.946*h2s + 19.887 * lactic


