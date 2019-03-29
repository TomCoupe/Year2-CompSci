# 201241037, Thomas Coupe, T.Coupe@student.liverpool.ac.uk
#When importing some of the libraries i had some issues as i was using windows. to import keras i had to use an anaconda environment
import random
import gym
import numpy as np
from collections import deque
from keras.models import Sequential
from keras.layers import Dense
from keras.optimizers import Adam

scores = []
#Declaring constants that will not change throughout the program
GAMMA = 0.95
LEARNING_RATE = 0.001
MEMORY_SIZE = 1000000
BATCH_SIZE = 20
EXPLORATION_MAX = 1.0
EXPLORATION_MIN = 0.01
EXPLORATION_DECAY = 0.995


class DQL: #all the methods within this class are for the neural network
    #this _init_ method is used to inisitalise the observation space and action space aswell as
    #setting the values for the neural network.
    def __init__(self, observation_space, action_space):
        self.exploration_rate = EXPLORATION_MAX
        self.action_space = action_space
        self.memory = deque(maxlen=MEMORY_SIZE)
        #the sequentail model is a linear layered model
        self.model = Sequential()
        #declaring the inputshape that will be used by the sequential model,
        #relu = Rectified Linear Unit
        self.model.add(Dense(24, input_shape=(observation_space,), activation="relu"))
        self.model.add(Dense(24, activation="relu"))
        self.model.add(Dense(self.action_space, activation="linear"))
        self.model.compile(loss="mse", optimizer=Adam(lr=LEARNING_RATE))

    def act(self, state):
        if np.random.rand() < self.exploration_rate:
            return random.randrange(self.action_space)
        q_values = self.model.predict(state)
        return np.argmax(q_values[0])

    def experience(self):
        if len(self.memory) < BATCH_SIZE:
            return
        batch = random.sample(self.memory, BATCH_SIZE)
        for state, action, reward, state_next, terminal in batch:
            q_update = reward
            if not terminal:
                #calculating the new Q value then adding it to the current state.
                q_update = (reward + GAMMA * np.amax(self.model.predict(state_next)[0]))
            q_values = self.model.predict(state)
            q_values[0][action] = q_update
            self.model.fit(state, q_values, verbose=0)
        self.exploration_rate *= EXPLORATION_DECAY
        self.exploration_rate = max(EXPLORATION_MIN, self.exploration_rate)
        
#when this method is called the values are saved into the memory which is then used by the agent.
    def rememberState(self, state, action, reward, next_state, done):
        self.memory.append((state, action, reward, next_state, done))


def cartpole():
    #creating the CartPole environment
    env = gym.make("CartPole-v1")
    #the observation space will be used to save possible state values
    #the action space is used to represent possible actions that can be performed by the agent.
    observation_space = env.observation_space.shape[0]
    action_space = env.action_space.n
    agent = DQL(observation_space, action_space)
    run = 0
    while True:
        #setting the number of attempts to 75 so the program does not loop forever.
        #after 75 attempts the starting score and the highest score acheived is output
        if run == 150:
           print ("==================")
           print ("150 runs completed")
           print ("==================")
           print ("Starting score = ", firstscore)
           print ("highest score obtained = ", max(scores))
           break
        run += 1
        #saving first score to compare it to the highest score acheived later
        if run == 2:
            firstscore = step
        #after each run we initalise a new environment 
        state = env.reset()
        state = np.reshape(state, [1, observation_space])
        step = 0
        while True:
        #for each step, based on a given state, we take an action from the agent and perform that action 
        #on the enviroment. we then receive a new state along with the reward for that state.
        #we then remember our state using the rememberState function. we then call experience.
            step += 1
            env.render()
            action = agent.act(state)
            state_next, reward, terminal, info = env.step(action)
            reward = reward if not terminal else -reward
            state_next = np.reshape(state_next, [1, observation_space])
            agent.rememberState(state, action, reward, state_next, terminal)
            state = state_next
            if terminal:
                print ("Run: " + str(run) + ", exploration: " + str(agent.exploration_rate) + ", score: " + str(step))
                scores.append(step)
                break
            agent.experience()

if __name__ == "__main__":
    cartpole()