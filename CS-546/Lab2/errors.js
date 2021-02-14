class MissingArgumentError extends Error{
    constructor(paramName){
        const message = `Missing Argument: ${paramName}.`
        super(message);
        this.message = message;
    }
}

class ArgumentTypeError extends Error{
    constructor(message){
        super(message);
        this.message = message;
    }
}

class InvalidRangeError extends Error{
    constructor(message){
        super(message);
        this.message = message;
    }
}

module.exports = {
    MissingArgumentError,
    ArgumentTypeError,
    InvalidRangeError
}