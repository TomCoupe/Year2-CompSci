#Thomas Coupe, 201241037, Assignment 01 - Advanced AI
import numpy as np
import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
import matplotlib.pyplot as plt
from sklearn.naive_bayes import GaussianNB,
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.metrics import accuracy_score, confusion_matrix
#loading the datasets below by using the read_csv() function from the pandas libary
columns = ['Pclass_1','Pclass_2','Pclass_3','Sex_female','Sex_male','Age_categories_Missing','Age_categories_Infant','Age_categories_Child','Age_categories_Teenager','Age_categories_Young Adult','Age_categories_Adult','Age_categories_Senior']
testdata = pd.read_csv('test.csv')
traindata = pd.read_csv('train.csv')

#printing the size of the dataset to ensure its not too small or not too large.
print("Dimensions of train data: {}".format(traindata.shape))
print("Dimensions of test data: {}".format(testdata.shape))

#this method checks the age of each passenger in the data file.
#all NaN values will be changed to -0.5. the datafile is changed
#then is returned.
def checkAge(datafile, cut_points, label_names):
    datafile["Age"] = datafile["Age"].fillna(-0.5)
    datafile["Age_categories"] = pd.cut(datafile["Age"],cut_points,labels=label_names)
    return datafile

#creating dummies for data entries in the datafile called in the parameters
def create_dummies(datafile,column_name):
    dummies = pd.get_dummies(datafile[column_name],prefix=column_name)
    datafile = pd.concat([datafile,dummies],axis=1)
    return datafile

cut_points = [-1,0,5,12,18,35,60,100]
label_names = ["Missing","Infant","Child","Teenager","Young Adult","Adult","Senior"]
traindata = checkAge(traindata,cut_points,label_names)
testdata = checkAge(testdata,cut_points,label_names)
#using a for loop to input dummy values into the datasets
#the for loop iterates through the three columns and creates the values.
for col in ["Pclass", "Sex", "Age_categories"]:
    traindata = create_dummies(traindata, col)
    testdata = create_dummies(testdata,col)


"""implimenting the Naive Bayes algorithm on the titanic dataset.
   outputting the accuracy score and the confusion matrix after outputting
   the data. the data is saved to  a csv file with all of the survival scores"""
def naiveBayes():
    holdout = testdata
    all_X = traindata[columns]
    all_y = traindata['Survived']
    #splitting the data
    train_X, test_X, train_y, test_y = train_test_split(all_X, all_y, test_size=0.20,random_state=0)

    #implimenting the naive bayes algorithm by assigning it to the nv variable
    nv = GaussianNB()
    nv.fit(all_X,all_y)

    #using cross validation on the dataset to record a score of accuracy.
    #all the recorded scores are then sorted and the average of the scores
    #are then defined as the average accuracy.
    scores = cross_val_score(nv, all_X, all_y, cv=10)
    scores.sort()
    accuracyNB = scores.mean()
    
    y_pred = nv.predict(test_X)
    holdout_predictions = nv.predict(holdout[columns])
    holdout_ids = holdout["PassengerId"]
    #writing the data to a new csv fie with the survival scores.
    submission_df = {"PassengerId": holdout_ids, "Survived": holdout_predictions}
    submission = pd.DataFrame(submission_df)
    submission.to_csv("submissionNB.csv",index=False)
    data = pd.read_csv('submissionNB.csv')
    cm = confusion_matrix(test_y, y_pred)
    #printing the data, the accuracy score and the confusion matrix
    print(data)
    print("Accuracy Score: ",accuracyNB)
    print("Confusion matrix: ")
    print(cm)

"""implimenting the KNN algorithm on the dataset with k = 5.
   also outputting the accuracy score and the confusion matrix
   after outputting the data. The data is then saved to a csv
   file with all of the survival scores."""
def k_NearestNeighbors():
    holdout = testdata
    all_X = traindata[columns]
    all_y = traindata['Survived']
    
    #splitting the data
    train_X, test_X, train_y, test_y = train_test_split(all_X, all_y, test_size=0.20,random_state=0)
    
    #implimenting the knn algorithm with k = 5 to the dataset.
    knn = KNeighborsClassifier(n_neighbors=5)
    knn.fit(all_X, all_y)
    
    #using cross validation on the dataset to record a score of accuracy.
    #all the recorded scores are then sorted and the average of the scores
    #are then defined as the average accuracy.
    scores = cross_val_score(knn, all_X, all_y, cv=10)
    scores.sort()
    accuracyKNN = scores.mean()
    
    y_pred = knn.predict(test_X)
    holdout_predictions = knn.predict(holdout[columns])
    holdout_ids = holdout["PassengerId"]
    submission_df = {"PassengerId": holdout_ids, "Survived": holdout_predictions}
    submission = pd.DataFrame(submission_df)
    #writing the data to a new csv fie with the survival scores.
    submission.to_csv("submissionKNN.csv",index=False)
    data = pd.read_csv('submissionKNN.csv')
    cm = confusion_matrix(test_y, y_pred)
    #outputting data, the accuracy score and the confusion matrix
    print(data)
    print("Accuracy Score: ",accuracyKNN)
    print("Confusion matrix:")
    print(cm)
    
naiveBayes()
k_NearestNeighbors()


