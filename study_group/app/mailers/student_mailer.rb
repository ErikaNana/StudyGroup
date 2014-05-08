class StudentMailer < ActionMailer::Base
  default from: "studybuddiesmanoa@gmail.com"

    def invitation_email(inviter, invitee, course, invited_group)
        @inviter = inviter
        @invitee = invitee
        @course = course
        @invited_group = invited_group
        email = @invitee.email + "@hawaii.edu"
        mail(to: email, subject: "You've Been Invited to a " + course.name + " Study Group!")
    end
end
