package net.kaleidos.weather


class CheckForecastJob {
    
    def alarmService
    
    static triggers = {
        // Info at http://grails.org/plugin/quartz
        //cronExpression: "s m h D M W Y"
        
        // Every hour
        cron name: 'checkForecast', cronExpression: "0 0 * * * ?"
        // Every minute (only for testing)
        //cron name: 'checkForecast', cronExpression: "0 * * ? * *"
    }

    def execute() {
        log.info "Executing checkForecast service"
        alarmService.checkForecast()
    }
}
