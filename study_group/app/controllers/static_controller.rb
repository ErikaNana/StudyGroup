class StaticController < ApplicationController

    def welcome
    end

    def create
    end

    def display_json
        #for now
        info = params[:createGroup]
        course = info[:class]

        # returns hash map
        @master = info[:user]
        members = info[:members]

        group_members = "";
        members.each do |member|
            name = member[:name]
            email = member[:email]
            group_members = group_members + email + ","
            #check if the member exists
            student = Student.find_by_email(email)
            if student.nil?
                #create a default student
                Student.create(name: name, email: email, student_type: "web")
            end
        end
        #get rid of the last comma
        group_members.chop!

        #need to do error checking on this later
        #construct the group
        @group = GroupInvited.create(name: course+@master, members: group_members, master: @master)

    end

    #method to check if person is in a group
    #method to add person to a group

    #creates the student and their schedule
    def display_schedule
        schedule = params[:userSchedule]
        @user = schedule[:user]

        # [{name: name, courseInfo: [{day: days, time: times}, {day: days}, time: times]}]
        courses = schedule[:courses]
        schedule = ""

        #create course objects
        @courseArray = Array.new
        #create arrays
        courses.each do |course|
            days = ""
            times = ""
            day_and_times = course[:courseInfo] #array of hashmaps

            #build strings
            day_and_times.each do |info|
                days = days + info[:day] + ","
                times = times + info[:time] + ","
            end
            #remove last commas
            days.chop!
            times.chop!

            #if doesn't exist, then create it
            add_course = nil
            course_name = course[:name]
            schedule = schedule + course_name + ","

            check_course = Course.find_by_name(course_name)
            if check_course
                @courseArray.push(check_course)
                @found = "found"
            else
                add_course = Course.create(name: course[:name], days:days, times:times)
                @found = "not found"
                #if it exists, just add that
                @courseArray.push(add_course)
            end
        end
        #create the student
        schedule.chop!
        #need to get master user's name --> CHANGE THIS LATER
        name = "TEST STUDENT"
        @student = Student.create(name: name, schedule: schedule, email: @user, student_type: "android")
    end
end
