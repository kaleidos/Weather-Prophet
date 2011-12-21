package net.kaleidos.weather


class NotifyUsersJob {
    def alarmService
    
    static triggers = {
        // Info at http://grails.org/plugin/quartz
        //cronExpression: "s m h D M W Y"
        
        // Every day at 20:00
        cron name: 'checkAlarms', cronExpression: "0 0 20 * * ?"
        // Every minute (only for testing)
        //cron name: 'checkAlarms', cronExpression: "0 * * ? * *"
    }

    def execute() {
        log.info "Executing checkAlarms service"
        alarmService.checkAlarms()
    }
}
