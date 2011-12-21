import ru.perm.kefir.asynchronousmail.*

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class AsynchronousMailController {
	
    static defaultAction = 'list';

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 100, 100)
        if (!params.sort) {
            params.sort = 'createDate';
        }
        if (!params.order) {
            params.order = 'desc';
        }
        [list: AsynchronousMailMessage.list(params), total: AsynchronousMailMessage.count()]
    }

	def show() {
		def message = getMsg(params.id)
		
		return [message: message]
	}
	
	def edit() {
		def message = getMsg(params.id)
		
		return [message: message]
	}
	
    def update() {
		def message = getMsg(params.id)
		
        bindData(message, params, [include:['status', 'beginDate', 'endDate', 'maxAttemptsCount', 'attemptInterval']]);
        message.attemptsCount = 0;
        if (!message.hasErrors() && message.save()) {
            flash.message = "Message ${params.id} updated"
            redirect(action:'show', id: message.id)
        } else {
            render(view: 'edit', model: [message: message])
        }
    }

    /** Abort message sent   */
    def abort() {
		def message = getMsg(params.id)
		
        if (message.status == MessageStatus.CREATED || message.status == MessageStatus.ATTEMPTED) {
            message.status = MessageStatus.ABORT;
            if (!message.save()) {
                flash.message = "Can't abort message with id ${message.id}";
            }
        } else {
            flash.message = "Can't abort message with id ${message.id} and status ${message.status}";
        }
        redirect(action: 'list');
    }

	
	protected getMsg(msgId) {
		def message = AsynchronousMailMessage.get(msgId)
		if (message) {
			return message
		} else {
			flash.message = "Message not found with id ${msgId}"
			redirect(action: 'list')
		}
	}
}
