import pandas as pd

FILE_PATH = "us-states.csv"
STATE, CASES = 'state', 'cases'

def summary_statistics(state: str):
    df = pd.read_csv(FILE_PATH)
    
    state = state.strip()
    if state == '' or not (state in df[STATE].unique()):
        raise ValueError(f"gives state ${state} is not present in data set!")
    
    state_df = df.loc[df[STATE] == state]
    state_cases = state_df[CASES]
    
    res = {
        'minimum': state_cases.min(),
        'maximum': state_cases.max(),
        'mean': state_cases.mean(),
        'sd': state_cases.std()
    }
    
    return res

def main():
    #The output should contain Minimum, Maximum, Standard deviation and Mean, each data in separate line in below given order:
    #Minimum:
    #Maximum:
    #Mean:
    #Standard Deviation:
    user_state = input("State:\n").strip()
    result = summary_statistics(user_state)
    display = f"Minimum: {result['minimum']}\nMaximum: {result['maximum']}\nMean: {result['mean']}\nStandard Deviation: {result['sd']}"
    print(display)
    

if __name__ == "__main__":
    main()